import {
  Component,
  computed,
  DestroyRef,
  inject,
  OnInit,
  signal,
} from '@angular/core';
import { StepperModule } from 'primeng/stepper';
import { Button, ButtonDirective } from 'primeng/button';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { DatasetFormat, DataUploadService } from './data-upload.service';
import { GnDatasetInfo } from 'gapi';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { DropdownModule } from 'primeng/dropdown';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { FileUploadEvent, FileUploadModule } from 'primeng/fileupload';
import { ChipModule } from 'primeng/chip';
import { SearchContextDirective } from '../../search/search-context.directive';
import { LanguagesLoaderDirective } from '../../language/languages-loader.directive';
import { SearchResultsLoaderDirective } from '../../search/search-results-loader.directive';
import { TableModule } from 'primeng/table';
import 'brace';
import 'brace/mode/xml';
import 'brace/theme/github';
import { AceModule } from 'ngx-ace-wrapper';

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
    FileUploadModule,
    ChipModule,
    SearchContextDirective,
    LanguagesLoaderDirective,
    SearchResultsLoaderDirective,
    TableModule,
    AceModule,
  ],
  styleUrl: './data-upload.component.css',
})
export class DataUploadComponent implements OnInit {
  template = signal('');
  datasource = signal(
    'https://sdi.eea.europa.eu/webdav/datastore/public/coe_t_emerald_p_2021-2022_v05_r00/Emerald_2022_BIOREGION.csv'
  );
  layername = signal('');

  dataUploadService = inject(DataUploadService);

  maxFileUploadSize = 100;
  supportedFormats = signal<DatasetFormat[]>([]);
  mainSupportedFormats = computed(() => {
    const DEFAULT_FORMATS = [
      'CSV',
      'XLS',
      'GeoJSON',
      'GPKG',
      'Parquet',
      'SHP',
      'WFS',
    ];
    return this.supportedFormats().filter(
      f => DEFAULT_FORMATS.indexOf(f.name) !== -1
    );
  });
  previewResult = signal<string>('');
  analysisResult = signal<GnDatasetInfo | undefined>(undefined);
  layers = signal<string[]>([]);

  isFetchingLayers = signal(false);
  isExecutingAnalysis = signal(false);
  isCreatingPreview = signal(false);

  isDatasourceAndTemplateSelected = computed(() => {
    return (
      this.template() !== '' &&
      this.datasource() !== '' &&
      this.layername() !== ''
    );
  });

  errorFetchingLayers = signal('');
  errorExecutingAnalysis = signal('');
  errorPreviewingAnalysis = signal('');

  crsCode = computed(() => {
    let output = '';
    let datasetInfo = this.analysisResult();
    if (datasetInfo?.layers && datasetInfo.layers[0].geometryFields) {
      let parsedValue = datasetInfo.layers[0].geometryFields[0].crs
        ?.split(',')
        .pop();
      output = parsedValue ? parsedValue.replace(']]', '') : '';
    }

    return output;
  });

  private destroyRef = inject(DestroyRef);

  ngOnInit(): void {
    this.dataUploadService.getFormats().subscribe(r => {
      this.supportedFormats.set(r);
    });
  }

  onStepChange(step: number) {
    if (step == 1) {
      if (!this.analysisResult()) {
        this.isExecutingAnalysis.set(true);
        this.errorExecutingAnalysis.set('');

        const subscription = this.dataUploadService
          .executeAnalysis(this.datasource(), this.layername())
          .subscribe({
            next: result => {
              this.analysisResult.set(result);
            },
            error: error => {
              console.log(error);
              this.errorExecutingAnalysis.set(error.errorMessage);
              this.isExecutingAnalysis.set(false);
            },
            complete: () => {
              this.isExecutingAnalysis.set(false);
            },
          });

        this.destroyRef.onDestroy(() => {
          subscription.unsubscribe();
        });
      }
    } else if (step == 2) {
      if (!this.previewResult()) {
        this.isCreatingPreview.set(true);
        this.errorPreviewingAnalysis.set('');

        const subscription = this.dataUploadService
          .previewAnalysis(this.template(), this.datasource(), this.layername())
          .subscribe({
            next: result => {
              this.previewResult.set(result);
            },
            error: error => {
              console.log(error);
              this.errorPreviewingAnalysis.set(error.errorMessage);
              this.isCreatingPreview.set(false);
            },
            complete: () => {
              this.isCreatingPreview.set(false);
            },
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

    const subscription = this.dataUploadService
      .retrieveLayers(this.datasource())
      .subscribe({
        next: result => {
          this.layers.set(result);
          if (result.length > 0) {
            this.layername.set(result[0]);
          }
        },
        error: error => {
          console.log(error);
          this.errorFetchingLayers.set(error.statusText);
          this.isFetchingLayers.set(false);
        },
        complete: () => {
          this.isFetchingLayers.set(false);
        },
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
    this.layername.set('');

    // Reset layers and analysis values
    this.layers.set([]);
    this.analysisResult.set(undefined);
    this.previewResult.set('');
  }

  onBasicUploadAuto(event: FileUploadEvent) {}
}
