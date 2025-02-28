import {
  Component,
  computed,
  effect,
  inject,
  OnInit,
  signal,
  ViewChild,
} from '@angular/core';
import { StepperModule } from 'primeng/stepper';
import { Button, ButtonDirective } from 'primeng/button';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import {
  CreateMetadataTypeEnum,
  CreateRequest,
  DeleteRecordRequest,
  GnDatasetInfo,
  GnRasterInfo,
  RecordsApi,
} from 'gapi';
import {
  AnalysisSynchMetadataResourceRequest,
  AnalysisSynchRequest,
  ApplyDataAnalysisOnRecordForMetadataResourceRequest,
  ApplyDataAnalysisOnRecordRequest,
  DataAnalysisControllerApi,
  DataFormat,
  GetAllResourcesRequest,
  LayersMetadataResourceRequest,
  LayersMetadataResourceVisibilityEnum,
  LayersRequest,
  MetadataResource,
  PreviewDataAnalysisOnRecordForMetadataResourceRequest,
  PreviewDataAnalysisOnRecordRequest,
  PutResourceRequest,
  RecordsApi as RecordsApi5,
  IndexRecord,
} from 'g5api';

import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { DropdownModule } from 'primeng/dropdown';
import {
  FileRemoveEvent,
  FileSelectEvent,
  FileUploadHandlerEvent,
  FileUploadModule,
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
import {
  API5_CONFIGURATION,
  API_CONFIGURATION,
} from '../../config/config.loader';
import { Select } from 'primeng/select';
import { Listbox, ListboxChangeEvent } from 'primeng/listbox';
import { Message } from 'primeng/message';
import { OverviewSelectorComponent } from '../overview-selector/overview-selector.component';
import { HttpClient } from '@angular/common/http';
import { SeparatorComponent } from '../../ui/separator/separator.component';

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
    Listbox,
    Message,
    OverviewSelectorComponent,
    SeparatorComponent,
  ],
  templateUrl: './new-record-panel.component.html',
  styleUrl: './new-record-panel.component.css',
})
export class NewRecordPanelComponent implements OnInit {
  @ViewChild('fileUpload', { static: false }) fileUpload: any;

  template = signal('');
  activeStep = signal(1);
  newRecordId = signal('');

  datasource = signal(
    'https://sdi.eea.europa.eu/webdav/datastore/public/coe_t_emerald_p_2021-2022_v05_r00/Emerald_2022_BIOREGION.csv'
  );
  datasourceFile = signal('');

  datasourceTypes = [
    {
      name: 'URL',
      prefix: '',
      placeholder: 'https://...',
      icon: 'fa-link',
    },
    {
      name: 'DB',
      prefix: '',
      placeholder: 'postgresql://www-data:www-data@localhost:5432/geo',
      icon: 'fa-database',
    },
    {
      name: 'WFS',
      prefix: 'WFS:',
      placeholder: 'https://...',
      icon: 'fa-map',
    },
    {
      name: 'ZIP',
      prefix: '/vsizip//vsicurl/',
      placeholder: 'https://...',
      icon: 'fa-archive',
    },
  ];
  datasourceType = signal(this.datasourceTypes[0]);

  datasourceWithPrefix = computed(() => {
    return (this.datasourceType().prefix || '') + this.datasource();
  });

  layername = signal('');
  metadataFiles = signal<MetadataResource[]>([]);

  errorMessage = signal('');

  api5Configuration = inject(API5_CONFIGURATION);
  apiConfiguration = inject(API_CONFIGURATION);

  http = inject(HttpClient);

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

    return this.supportedFormats().filter(f =>
      f.name ? DEFAULT_FORMATS.indexOf(f.name) !== -1 : false
    );
  });
  previewResult = signal<string>('');
  analysisResult = signal<IndexRecord | undefined | undefined>(undefined);
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
    return this.datasourceFile() !== '';
  });

  errorFetchingLayers = signal('');
  errorExecutingAnalysis = signal('');
  errorPreviewingAnalysis = signal('');

  private stepEvents: { [key: number]: Function } = {
    2: this.editRecord.bind(this),
    3: this.getDatasetInfo.bind(this),
    4: this.buildOverview.bind(this),
    5: this.getRecordPreview.bind(this),
  };

  constructor() {
    effect(() => {
      this.stepEvents[this.activeStep()] &&
        this.stepEvents[this.activeStep()]();
    });
  }

  ngOnInit(): void {
    // TODO: Check GDAL is available
    // Retrieve the dataset formats supported
    this.dataAnalysisApi()
      .formats()
      .then(
        response => {
          this.supportedFormats.set(response);
        },
        error => {
          console.log(
            'Error retrieving the dataset formats supported: ' + error.response
          );
          this.errorMessage.set(
            'Error retrieving the dataset formats supported: ' +
              error.response.statusText
          );
        }
      );
  }

  editRecord() {
    this.createRecord(false);
  }

  openEditor() {
    const target = top?.window.location || location;
    target.replace(
      this.api5Configuration().basePath +
        `/srv/eng/catalog.edit#/metadata/${this.newRecordId()}`
    );
  }

  /**
   * Creates a new metadata record.
   *
   * @param redirectToEditor if true, opens the metadata editor with the new metadata.
   */
  createRecord(redirectToEditor: boolean) {
    if (!this.isTemplateSelected()) {
      return;
    }
    if (this.isRecordCreated()) {
      if (redirectToEditor) {
        this.openEditor();
      }
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
          Accept: 'application/json', // Accept could be all?
          'Content-Type': 'application/json',
        },
      })
      .then(
        response => {
          this.newRecordId.set(response);
          this.isCreatingRecord.set(false);
          if (redirectToEditor) {
            this.openEditor();
          }
        },
        error => {
          console.log(
            'Error creating the metadata record: ' + error.response.statusText
          );
          this.errorMessage.set(
            'Error creating the metadata record: ' + error.response.statusText
          );
          this.activeStep.set(1);
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
      datasource: this.datasourceWithPrefix(),
    };

    this.dataAnalysisApi()
      .layers(layersRequest)
      .then(
        response => {
          this.layers.set(response);
          if (response.length > 0) {
            this.layername.set(response[0]);
          }
          this.isFetchingLayers.set(false);
        },
        error => {
          console.log(
            'Error retrieving the dataset layers: ' + error.response.statusText
          );
          this.errorFetchingLayers.set(error.statusText);
          this.isFetchingLayers.set(false);
        }
      );
  }

  /**
   * Retrieves the layers list for a datasource uploaded to a metadata.
   */
  private getLayerListForMetadataResource() {
    if (!this.datasourceFile()) {
      return;
    }

    this.isFetchingLayers.set(true);
    this.errorFetchingLayers.set('');

    const layersRequest: LayersMetadataResourceRequest = {
      metadataUuid: this.newRecordId(),
      datasource: this.datasourceFile(),
      visibility: LayersMetadataResourceVisibilityEnum.Public,
      approved: true,
    };

    this.dataAnalysisApi()
      .layersMetadataResource(layersRequest)
      .then(
        response => {
          this.layers.set(response);
          if (response.length > 0) {
            this.layername.set(response[0]);
          }
          this.isFetchingLayers.set(false);
        },
        error => {
          console.log(
            'Error retrieving the dataset layers: ' + error.response.statusText
          );
          this.errorFetchingLayers.set(error.statusText);
          this.isFetchingLayers.set(false);
        }
      );
  }

  /**
   * Analyses the selected dataset, returning the related information.
   */
  private getDatasetInfo() {
    if (this.analysisResult()) return;

    this.isExecutingAnalysis.set(true);
    this.errorExecutingAnalysis.set('');

    const analysisRequest = this.datasourceFile()
      ? {
          metadataUuid: this.newRecordId(),
          datasource: this.datasourceFile(),
          visibility: LayersMetadataResourceVisibilityEnum.Public,
          approved: true,
          layer: this.layername(),
        }
      : {
          datasource: this.datasourceWithPrefix(),
          layer: this.layername(),
        };

    const analysisMethod = this.datasourceFile()
      ? this.dataAnalysisApi().analysisSynchMetadataResource(
          analysisRequest as AnalysisSynchMetadataResourceRequest
        )
      : this.dataAnalysisApi().analysisSynchIndexRecord(analysisRequest);

    analysisMethod.then(
      response => {
        this.analysisResult.set(response);
        this.applyAnalysisToRecord();
        this.isExecutingAnalysis.set(false);
      },
      error => {
        console.log('Error processing the dataset: ', error);
        this.errorExecutingAnalysis.set(error.errorMessage);
        this.isExecutingAnalysis.set(false);
      }
    );
  }

  overviewFromData = signal<Blob | undefined>(undefined);
  buildingOverview = signal(false);

  buildOverview() {
    if (
      this.analysisResult() === undefined ||
      (this.analysisResult() && this.analysisResult()!.geom === undefined)
    ) {
      return;
    }

    this.overviewFromData.set(undefined);
    this.buildingOverview.set(true);

    const baseUrl =
        this.api5Configuration().basePath + '/api/data/analysis/overview',
      overviewUrl = !this.datasourceFile()
        ? `${baseUrl}?datasource=${encodeURIComponent(this.datasourceWithPrefix())}&layer=${this.layername()}`
        : `${baseUrl}ForMetadataResource?uuid=${this.newRecordId()}&visibility=${LayersMetadataResourceVisibilityEnum.Public}&approved=true&datasource=${encodeURIComponent(this.datasourceFile())}&layer=${this.layername()}`;
    this.http
      .get(overviewUrl, {
        responseType: 'blob',
      })
      .subscribe({
        next: image => {
          if (image.size > 0) {
            this.overviewFromData.set(image);
          } else {
            this.errorMessage.set('Empty image returned.');
          }
          this.buildingOverview.set(false);
        },
        error: error => {
          this.errorMessage.set(
            'Error building the overview: ' + error.statusText
          );
          this.buildingOverview.set(false);
        },
      });
  }

  /**
   * Previews the metadata record with the dataset analysis result.
   */
  private getRecordPreview() {
    if (this.previewResult()) return;

    this.isCreatingPreview.set(true);
    this.errorPreviewingAnalysis.set('');

    const previewAnalysisRequest = this.datasourceFile()
      ? {
          metadataUuid: this.newRecordId(),
          datasource: this.datasourceFile(),
          visibility: LayersMetadataResourceVisibilityEnum.Public,
          approved: true,
          layer: this.layername(),
        }
      : {
          uuid: this.newRecordId(),
          datasource: this.datasourceWithPrefix(),
          layer: this.layername(),
        };

    const previewMethod = this.datasourceFile()
      ? this.dataAnalysisApi().previewDataAnalysisOnRecordForMetadataResource(
          previewAnalysisRequest as PreviewDataAnalysisOnRecordForMetadataResourceRequest
        )
      : this.dataAnalysisApi().previewDataAnalysisOnRecord(
          previewAnalysisRequest as PreviewDataAnalysisOnRecordRequest
        );

    previewMethod.then(
      response => {
        this.previewResult.set(response);
        this.isCreatingPreview.set(false);
      },
      error => {
        console.log(
          'Error previewing metadata dataset analysis: ' +
            error.response.statusText
        );
        this.errorPreviewingAnalysis.set(error.errorMessage);
        this.isCreatingPreview.set(false);
      }
    );
  }

  /**
   * Updates the metadata record with the dataset analysis result.
   */
  applyAnalysisToRecord() {
    const analysisRequest = this.datasourceFile()
      ? {
          uuid: this.newRecordId(),
          datasource: this.datasourceFile(),
          visibility: LayersMetadataResourceVisibilityEnum.Public,
          approved: true,
          layer: this.layername(),
        }
      : {
          uuid: this.newRecordId(),
          datasource: this.datasourceWithPrefix(),
          layer: this.layername(),
        };

    const applyAnalysis = this.datasourceFile()
      ? this.dataAnalysisApi().applyDataAnalysisOnRecordForMetadataResource(
          analysisRequest as ApplyDataAnalysisOnRecordForMetadataResourceRequest
        )
      : this.dataAnalysisApi().applyDataAnalysisOnRecord(analysisRequest);

    applyAnalysis.then(
      () => {
        this.recordsApi().index({ uuids: [this.newRecordId()] });
        // .then(() => {
        //   this.openEditor();
        // });
      },
      error => {
        console.log(
          'Error updating metadata record with the dataset analysis: ' +
            error.response.statusText
        );
        this.errorMessage.set(
          'Error updating metadata record with the dataset analysis: ' +
            error.response.statusText
        );
      }
    );
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

  resetForm() {
    this.template.set('');
    this.newRecordId.set('');
    this.onDatasourceChange();
    this.activeStep.set(1);
  }

  onAttachmentSelected(event: FileSelectEvent) {
    this.onDatasourceChange();
  }

  /**
   * Event triggered to upload the selected files to the metadata.
   */
  onUploadHandler(event: FileUploadHandlerEvent) {
    if (!this.newRecordId()) {
      return;
    }

    for (let i = 0; i < event.files.length; i++) {
      const putResourceRequest: PutResourceRequest = {
        metadataUuid: this.newRecordId(),
        file: event.files[i],
      };

      this.recordsDataStoreApi()
        .putResource(putResourceRequest)
        .then(
          response => {
            // select the file by default, when uploading 1 file only
            this.retrieveMetadataFiles(event.files.length == 1);
          },
          error => {
            console.log(
              'Error uploading file to metadata: ' + error.response.statusText
            );
            this.errorMessage.set(
              'Error uploading file to metadata: ' + error.response.statusText
            );
          }
        );
    }

    this.fileUpload.clear();
  }

  onRemove(event: FileRemoveEvent) {
    this.onDatasourceChange();
  }

  onClear(event: any) {
    this.onDatasourceChange();
  }

  /**
   * Event triggered when selecting a metadata file dataset to extract the list of layers available to process.
   *
   */
  onMetadataFileSelectedChange($event: ListboxChangeEvent) {
    if ($event.value) {
      this.datasourceFile.set($event.value);
      this.getLayerListForMetadataResource();
    } else {
      this.onDatasourceChange();
    }
  }

  /**
   * Event triggered to cancel the data analysis process.
   *
   * It resets the form and removes the related metadata.
   */
  onNewRecordCancel() {
    if (this.isRecordCreated()) {
      const deleteRecordRequest: DeleteRecordRequest = {
        metadataUuid: this.newRecordId(),
      };

      this.recordsApi()
        .deleteRecord(deleteRecordRequest)
        .then(
          response => {
            this.resetForm();
          },
          error => {
            console.log(
              'Error deleting the metadata: ' + error.response.statusText
            );
            this.errorMessage.set(
              'Error deleting the metadata: ' + error.response.statusText
            );
          }
        );
    } else {
      this.resetForm();
    }
  }

  private retrieveMetadataFiles(selectFile: boolean): void {
    const getAllResourcesRequest: GetAllResourcesRequest = {
      metadataUuid: this.newRecordId(),
    };

    this.recordsDataStoreApi()
      .getAllResources(getAllResourcesRequest)
      .then(
        response => {
          this.metadataFiles.set(response);

          if (response.length > 0 && selectFile) {
            this.datasourceFile.set(response[0].filename!);
            this.getLayerListForMetadataResource();
          }
        },
        error => {
          console.log(
            'Error retrieving the metadata files: ' + error.response.statusText
          );
          this.errorMessage.set(
            'Error retrieving the metadata files: ' + error.response.statusText
          );
        }
      );
  }

  protected readonly ResourceTypeLayout = ResourceTypeLayout;
}
