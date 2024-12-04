import {Component, computed, DestroyRef, inject, OnInit, signal,} from '@angular/core';
import {StepperModule} from 'primeng/stepper';
import {Button, ButtonDirective} from 'primeng/button';
import {FormsModule} from '@angular/forms';
import {InputTextModule} from 'primeng/inputtext';
import {DatasetFormat, DataUploadService,} from '../data-upload/data-upload.service';
import {CreateMetadataTypeEnum, CreateRequest, GnDatasetInfo, GnRasterInfo, RecordsApi} from 'gapi';
import {InputGroupModule} from 'primeng/inputgroup';
import {InputGroupAddonModule} from 'primeng/inputgroupaddon';
import {DropdownModule} from 'primeng/dropdown';
import {FileUploadEvent, FileUploadModule} from 'primeng/fileupload';
import {ChipModule} from 'primeng/chip';
import 'brace';
import 'brace/mode/xml';
import 'brace/theme/github';
import {AceModule} from 'ngx-ace-wrapper';
import {ResourceTypeLayout} from '../../record/record-field-resource-type/record-field-resource-type.component';
import {DataAnalysisPanelComponent} from '../data-analysis-panel/data-analysis-panel.component';
import {TemplatesSelectorComponent} from '../templates-selector/templates-selector.component';
import {PrimeTemplate} from 'primeng/api';
import {ProgressBarModule} from 'primeng/progressbar';
import {API_CONFIGURATION} from "../../config/config.loader";

@Component({
  selector: 'g-new-record-panel',
  standalone: true,
  imports: [
    AceModule,
    Button,
    ButtonDirective,
    ChipModule,
    DataAnalysisPanelComponent,
    DropdownModule,
    FileUploadModule,
    FormsModule,
    InputGroupAddonModule,
    InputGroupModule,
    InputTextModule,
    PrimeTemplate,
    ProgressBarModule,
    StepperModule,
    TemplatesSelectorComponent,
  ],
  templateUrl: './new-record-panel.component.html',
  styleUrl: './new-record-panel.component.css',
})
export class NewRecordPanelComponent implements OnInit {
  template = signal('');
  newRecordId = signal('');
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
      'GTiff',
    ];
    return this.supportedFormats().filter(
      f => DEFAULT_FORMATS.indexOf(f.name) !== -1
    );
  });
  previewResult = signal<string>('');
  analysisResult = signal<GnDatasetInfo | GnRasterInfo | undefined>(undefined);
  layers = signal<string[]>([]);

  isCreatingRecord = signal(false);
  isFetchingLayers = signal(false);
  isExecutingAnalysis = signal(false);
  isCreatingPreview = signal(false);

  isTemplateSelected = computed(() => {
    return this.template() !== '';
  });
  isRecordCreated = computed(() => {
    return this.newRecordId() !== '';
  });
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

  apiConfiguration = inject(API_CONFIGURATION);

  recordsApi = computed(() => {
    return new RecordsApi(this.apiConfiguration());
  });

  private destroyRef = inject(DestroyRef);

  ngOnInit(): void {
    this.dataUploadService.getFormats().subscribe(r => {
      this.supportedFormats.set(r);
    });
  }

  private stepEvents: { [key: number]: Function } = {
    2: this.getDatasetInfo.bind(this),
    3: this.getRecordPreview.bind(this),
  };

  onStepChange(step: number) {
    this.stepEvents[step] && this.stepEvents[step]();
  }

  editRecord() {
    this.createRecord(true);
  }

  createRecord(redirectToEditor: boolean) {
    if (!this.isTemplateSelected()) {
      return;
    }
    if (this.newRecordId()) {
      return;
    }

    this.isCreatingRecord.set(true);
    const createRequest: CreateRequest = {
      sourceUuid: this.template(),
      metadataType: CreateMetadataTypeEnum.Metadata,
      group: "1", // TODO
      allowEditGroupMembers: false
    };

    this.recordsApi().create(createRequest, {
      headers: { // TODO: remove this? Header should be set in the gapi?
        "X-XSRF-TOKEN": "c6e73fb8-0803-4f25-b515-d0a21bab9b0c",
        "Accept": "application/json", // Accept could be all?
        "Content-Type": "application/json"
      }
    }).then(response => {
      this.newRecordId.set(response);
      this.isCreatingRecord.set(false);
      if (redirectToEditor) {
        location.replace(`/geonetwork/srv/eng/catalog.edit#/metadata/${this.newRecordId()}`);
      }
    }, error  => {
      console.error(error);
      this.isCreatingRecord.set(false);
    });
  }


  getLayerList() {
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

  private getDatasetInfo() {
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
  }

  private getRecordPreview() {
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

  onBasicUploadAuto(event: FileUploadEvent) {
  }

  moveToConfirmationPanel() {
  }

  protected readonly ResourceTypeLayout = ResourceTypeLayout;
}
