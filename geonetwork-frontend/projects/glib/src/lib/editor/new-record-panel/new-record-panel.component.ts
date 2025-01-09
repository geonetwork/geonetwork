import {
  Component,
  computed,
  DestroyRef,
  effect,
  inject,
  OnInit,
  signal,
} from '@angular/core';
import { StepperModule } from 'primeng/stepper';
import { Button, ButtonDirective } from 'primeng/button';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import {
  CreateMetadataTypeEnum,
  CreateRequest,
  GnDatasetInfo,
  GnRasterInfo,
  RecordsApi,
} from 'gapi';
import {
  DataAnalysisControllerApi,
  DataFormat,
  RecordsApi as RecordsApi5,
  PutResourceRequest,
  AnalysisSynchMetadataResourceRequest,
  AnalysisSynchRequest,
  ApplyDataAnalysisOnRecordRequest, ApplyDataAnalysisOnRecordForMetadataResourceRequest,
  LayersMetadataResourceRequest, LayersMetadataResourceVisibilityEnum,
  LayersRequest, PreviewDataAnalysisOnRecordForMetadataResourceRequest,
  PreviewDataAnalysisOnRecordRequest
} from 'g5api';

import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { DropdownModule } from 'primeng/dropdown';
import {
  FileRemoveEvent,
  FileSelectEvent,
  FileUploadEvent,
  FileUploadHandlerEvent,
  FileUploadModule
} from 'primeng/fileupload';
import { ChipModule } from 'primeng/chip';
import 'brace';
import 'brace/mode/xml';
import 'brace/theme/github';
import { AceModule } from 'ngx-ace-wrapper';
import { ResourceTypeLayout } from '../../record/record-field-resource-type/record-field-resource-type.component';
import { DataAnalysisPanelComponent } from '../data-analysis-panel/data-analysis-panel.component';
import { TemplatesSelectorComponent } from '../templates-selector/templates-selector.component';
import { PrimeTemplate } from 'primeng/api';
import { ProgressBarModule } from 'primeng/progressbar';
import {API5_CONFIGURATION, API_CONFIGURATION} from '../../config/config.loader';
import { Select } from 'primeng/select';

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
    Select,
  ],
  templateUrl: './new-record-panel.component.html',
  styleUrl: './new-record-panel.component.css',
})
export class NewRecordPanelComponent implements OnInit {
  template = signal('');
  activeStep = signal(1);
  newRecordId = signal('');
  //newRecordUuid = signal('');
  datasource = signal(
    'https://sdi.eea.europa.eu/webdav/datastore/public/coe_t_emerald_p_2021-2022_v05_r00/Emerald_2022_BIOREGION.csv'
  );
  datasourceFile = signal('');
  layername = signal('');

  api5Configuration = inject(API5_CONFIGURATION);
  apiConfiguration = inject(API_CONFIGURATION);

  dataAnalysisApi = computed(() => {
    return new DataAnalysisControllerApi(this.api5Configuration());
  });

  recordsDataStoreApi = computed(() => {
    return new RecordsApi5(this.api5Configuration());
  });

  recordsApi = computed(() => {
    return new RecordsApi(this.apiConfiguration());
  });

  maxFileUploadSize = 100;
  /* Dataset supported formats */
  supportedFormats = signal<DataFormat[]>([]);
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
      f => f.name ? DEFAULT_FORMATS.indexOf(f.name) !== -1 : false
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

  isDatasourceFileSelected = computed(() => {
    return this.datasourceFile() !== ''
  });

  errorFetchingLayers = signal('');
  errorExecutingAnalysis = signal('');
  errorPreviewingAnalysis = signal('');

  private stepEvents: { [key: number]: Function } = {
    2: this.editRecord.bind(this),
    3: this.getDatasetInfo.bind(this),
    4: this.getRecordPreview.bind(this),
  };

  constructor() {
    effect(() => {
      this.stepEvents[this.activeStep()] &&
        this.stepEvents[this.activeStep()]();
    });
  }

  ngOnInit(): void {
    // Retrieve the dataset formats supported
    this.dataAnalysisApi()
      .formats().then(
        response => {
          this.supportedFormats.set(response);
        },
      error => {
        console.error(error);
      }
    );
  }

  editRecord() {
    this.createRecord(false);
  }

  createRecord(redirectToEditor: boolean) {
    if (!this.isTemplateSelected()) {
      return;
    }
    if (this.isRecordCreated()) {
      return;
    }

    this.isCreatingRecord.set(true);
    const createRequest: CreateRequest = {
      sourceUuid: this.template(),
      metadataType: CreateMetadataTypeEnum.Metadata,
      group: '1', // TODO
      allowEditGroupMembers: false,
    };

    this.recordsApi()
      .create(createRequest, {
        headers: {
          // TODO: remove this? Header should be set in the gapi?
          'X-XSRF-TOKEN': '17666fa5-607b-41d1-92ed-e1819c7da3b6',
          Accept: 'application/json', // Accept could be all?
          'Content-Type': 'application/json',
        },
      })
      .then(
        response => {
          this.newRecordId.set(response);
          this.isCreatingRecord.set(false);
          if (redirectToEditor) {
            location.replace(
              `/geonetwork/srv/eng/catalog.edit#/metadata/${this.newRecordId()}`
            );
          }
        },
        error => {
          console.error(error);
          this.isCreatingRecord.set(false);
        }
      );
  }

  /**
   * Retrieves the layers list for a remote datasource.
   */
  getLayerList() {
    this.isFetchingLayers.set(true);
    this.errorFetchingLayers.set('');

    const layersRequest: LayersRequest = {
      datasource: this.datasource()
    }

    this.dataAnalysisApi().layers(layersRequest)
      .then(
        response => {
          this.layers.set(response);
          if (response.length > 0) {
            this.layername.set(response[0]);
          }
          this.isFetchingLayers.set(false);
        },
        error => {
          console.log(error);
          this.errorFetchingLayers.set(error.statusText);
          this.isFetchingLayers.set(false);
        }
    );
  }

  /**
   * Retrieves the layers list for a datasource uploaded to a metadata.
   */
  private getLayerListForMetadataResource() {
    this.isFetchingLayers.set(true);
    this.errorFetchingLayers.set('');

    const layersRequest: LayersMetadataResourceRequest = {
      metadataUuid: this.newRecordId(),
      datasource: this.datasourceFile(),
      visibility: LayersMetadataResourceVisibilityEnum.Public,
      approved: true
    }

    this.dataAnalysisApi().layersMetadataResource(layersRequest)
      .then(
        response => {
          this.layers.set(response);
          if (response.length > 0) {
            this.layername.set(response[0]);
          }
          this.isFetchingLayers.set(false);
        },
        error => {
          console.log(error);
          this.errorFetchingLayers.set(error.statusText);
          this.isFetchingLayers.set(false);
        }
      );
  }

  private getDatasetInfo() {
    if (!this.analysisResult()) {
      this.isExecutingAnalysis.set(true);
      this.errorExecutingAnalysis.set('');

      if (!this.datasourceFile()) {
        // Process dataset from external URL
        const analysisRequest: AnalysisSynchRequest = {
          datasource: this.datasource(),
          layer: this.layername()
        }

        this.dataAnalysisApi().analysisSynch(analysisRequest)
          .then(
            response => {
              this.analysisResult.set(response);
              this.isExecutingAnalysis.set(false);
            },
            error => {
              console.log(error);
              this.errorExecutingAnalysis.set(error.errorMessage);
              this.isExecutingAnalysis.set(false);
            }
          );
      } else {
        // Process uploaded dataset to a new metadata
        const analysisRequest: AnalysisSynchMetadataResourceRequest = {
          metadataUuid: this.newRecordId(),
          datasource: this.datasourceFile(),
          visibility: LayersMetadataResourceVisibilityEnum.Public,
          approved: true,
          layer: this.layername()
        }

        this.dataAnalysisApi().analysisSynchMetadataResource(analysisRequest)
          .then(
            response => {
              this.analysisResult.set(response);
              this.isExecutingAnalysis.set(false);
            },
            error => {
              console.log(error);
              this.errorExecutingAnalysis.set(error.errorMessage);
              this.isExecutingAnalysis.set(false);
            }
          );
      }

    }
  }

  private getRecordPreview() {
    if (!this.previewResult()) {
      this.isCreatingPreview.set(true);
      this.errorPreviewingAnalysis.set('');

      if (!this.datasourceFile()) {
        // Process dataset from external URL
        const previewAnalysisRequest: PreviewDataAnalysisOnRecordRequest = {
          uuid: this.template(),
          datasource: this.datasource(),
          layer: this.layername()
        }

        this.dataAnalysisApi().previewDataAnalysisOnRecord(previewAnalysisRequest)
          .then(
            response => {
              this.previewResult.set(response);
              this.isCreatingPreview.set(false);
            },
            error => {
              console.log(error);
              this.errorPreviewingAnalysis.set(error.errorMessage);
              this.isCreatingPreview.set(false);
            }
          );

      } else {
        // Process uploaded dataset to a new metadata
        const previewAnalysisRequest: PreviewDataAnalysisOnRecordForMetadataResourceRequest = {
          metadataUuid: this.newRecordId(),
          datasource: this.datasourceFile(),
          visibility: LayersMetadataResourceVisibilityEnum.Public,
          approved: true,
          layer: this.layername()
        }

        this.dataAnalysisApi().previewDataAnalysisOnRecordForMetadataResource(previewAnalysisRequest)
          .then(
            response => {
              this.previewResult.set(response);
              this.isCreatingPreview.set(false);
            },
            error => {
              console.log(error);
              this.errorPreviewingAnalysis.set(error.errorMessage);
              this.isCreatingPreview.set(false);
            }
          );
      }

    }
  }

  applyAnalysisToRecord() {
    if (!this.datasourceFile()) {
      const analysisRequest: ApplyDataAnalysisOnRecordRequest = {
        uuid: this.newRecordId(),
        datasource: this.datasource(),
        layer: this.layername()
      }

      this.dataAnalysisApi().applyDataAnalysisOnRecord(analysisRequest)
        .then(
          response => {
            location.replace(
              `/geonetwork/srv/eng/catalog.edit#/metadata/${this.newRecordId()}`
            );
          },
          error => {
            console.log(error);
          }
        );
    } else {
      const analysisRequest: ApplyDataAnalysisOnRecordForMetadataResourceRequest = {
        uuid: this.newRecordId(),
        datasource: this.datasourceFile(),
        visibility: LayersMetadataResourceVisibilityEnum.Public,
        approved: true,
        layer: this.layername()
      }

      this.dataAnalysisApi().applyDataAnalysisOnRecordForMetadataResource(analysisRequest)
        .then(
          response => {
            location.replace(
              `/geonetwork/srv/eng/catalog.edit#/metadata/${this.newRecordId()}`
            );
          },
          error => {
            console.log(error);
          }
        );
    }
  }

  onLayerChange(): void {
    // Reset analysis values
    this.analysisResult.set(undefined);
    this.previewResult.set('');
  }

  onDatasourceChange() {
    this.layername.set('');

    this.datasourceFile.set('');

    // Reset layers and analysis values
    this.layers.set([]);
    this.analysisResult.set(undefined);
    this.previewResult.set('');
  }

  onAttachmentSelected(event: FileSelectEvent) {
    this.onDatasourceChange();
  }

  onUploadHandler(event: FileUploadHandlerEvent) {
    console.log('selected attachment file:', event.files)

    if (!this.newRecordId()) {
      return;
    }

    const putResourceRequest: PutResourceRequest = {
      metadataUuid: this.newRecordId(),
      file: event.files[0],
    }

    this.recordsDataStoreApi().putResource(putResourceRequest)
      .then(
        response => {
          if (response.filename) {
            this.datasourceFile.set(response.filename);
            this.getLayerListForMetadataResource();
          }
        },
        error => {
          console.error(error);
        }
      );
  }

  onRemove(event: FileRemoveEvent) {
    console.log("onRemove")
    console.log(event);
    this.onDatasourceChange();
  }

  onClear(event: any) {
    console.log("onClear")
    console.log(event);
    this.onDatasourceChange();
  }
  moveToConfirmationPanel() {}

  protected readonly ResourceTypeLayout = ResourceTypeLayout;
}
