import {Component, computed, DestroyRef, inject, Signal, signal} from '@angular/core';
import { StepperModule } from 'primeng/stepper';
import {Button, ButtonDirective} from "primeng/button";
import {FormsModule} from "@angular/forms";
import {InputTextModule} from "primeng/inputtext";
import {DataUploadService} from "./data-upload.service";
import {GnDatasetInfo} from "gapi";
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import {DropdownModule} from "primeng/dropdown";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {FileUploadEvent, FileUploadModule} from "primeng/fileupload";

@Component({
  selector: 'g-data-upload',
  templateUrl: './data-upload.component.html',
  standalone: true,
  imports: [
    StepperModule,
    Button,
    FormsModule,
    InputTextModule,
    InputGroupModule,
    InputGroupAddonModule,
    ButtonDirective,
    DropdownModule,
    ProgressSpinnerModule,
    FileUploadModule
  ],
  styleUrl: './data-upload.component.css'
})
export class DataUploadComponent {
  template: string = 'd752fab9-4560-4a32-9da6-227b51fca867';
  datasource: string = 'https://sdi.eea.europa.eu/webdav/datastore/public/coe_t_emerald_p_2021-2022_v05_r00/Emerald_2022_BIOREGION.csv';
  layername: string = '';

  dataUploadService = inject(DataUploadService);

  previewResult = signal<string>('');
  analysisResult= signal<GnDatasetInfo | undefined>(undefined);
  layers = signal<string[]>([]);

  isFetchingLayers = signal(false);
  isExecutingAnalysis = signal(false);
  isCreatingPreview = signal(false);
  errorFetchingLayers = signal('');

  crsCode = computed(() => {
    let output = "";
    let datasetInfo = this.analysisResult();
    if (datasetInfo?.layers && datasetInfo.layers[0].geometryFields) {
      let parsedValue = datasetInfo.layers[0].geometryFields[0].crs?.split(",").pop();
      output = parsedValue ? parsedValue.replace("]]", "") : "";
    }

    return output;
  });


  private destroyRef = inject(DestroyRef);

  onStepChange(step: number) {
    if (step == 1) {
      if (!this.analysisResult()) {
        this.isExecutingAnalysis.set(true);

        const subscription = this.dataUploadService.executeAnalysis(this.datasource, this.layername).subscribe({
          next: result => {
            this.analysisResult.set(result);
          },
          error: error => {
            console.log(error);
            this.isExecutingAnalysis.set(false);
          },
          complete: () => {
            this.isExecutingAnalysis.set(false);
          }
        });

        this.destroyRef.onDestroy(() => {
          subscription.unsubscribe();
        });
      }
    } else if (step == 2) {
      if (!this.previewResult()) {
        this.isCreatingPreview.set(true);

        const subscription = this.dataUploadService.previewAnalysis(this.template, this.datasource, this.layername).subscribe({
          next: result => {
            this.previewResult.set(result);
          },
          error: error => {
            console.log(error);
            this.isCreatingPreview.set(false);
          },
          complete: () => {
            this.isCreatingPreview.set(false);
          }
        });

        this.destroyRef.onDestroy(() => {
          subscription.unsubscribe();
        });
      }
    }
  }

  retrieveLayers() {
    this.isFetchingLayers.set(true);
    this.errorFetchingLayers.set('');

    const subscription = this.dataUploadService.retrieveLayers(this.datasource).subscribe({
      next: result => {
        this.layers.set(result);
      },
      error: error => {
        console.log(error);
        this.errorFetchingLayers.set(error.statusText);
        this.isFetchingLayers.set(false);
      },
      complete: () => {
        this.isFetchingLayers.set(false);
      }
    });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }

  onLayerChange(): void {
    // Reset analysis values
    this.analysisResult.set(undefined);
    this.previewResult.set('');
  }

  onDatasourceChange() {
    this.layername = '';

    // Reset layers and analysis values
    this.layers.set([]);
    this.analysisResult.set(undefined);
    this.previewResult.set('');
  }

  onBasicUploadAuto(event: FileUploadEvent) {

  }
}
