import { Component, computed, inject, input, signal } from '@angular/core';
import { RadioButton } from 'primeng/radiobutton';
import { FormsModule } from '@angular/forms';
import { FileUpload, FileUploadHandlerEvent } from 'primeng/fileupload';
import { RecordsApi } from 'gapi';
import {
  API5_CONFIGURATION,
  API_CONFIGURATION,
} from '../../config/config.module';
import { Button } from 'primeng/button';
import { PutResourceRequest, RecordsApi as RecordsApi5 } from 'g5api';

@Component({
  selector: 'g-overview-selector',
  imports: [RadioButton, FormsModule, FileUpload, Button],
  templateUrl: './overview-selector.component.html',
  standalone: true,
})
export class OverviewSelectorComponent {
  uuid = input.required<string>();
  suggestion = input<Blob | undefined>();
  suggestionData = computed(() => {
    return URL.createObjectURL(this.suggestion()!);
  });

  overviewAttachedToRecord = signal(false);

  file = signal<Blob | undefined>(undefined);

  canSaveOverview = computed(() => {
    return (
      ((this.suggestion() !== undefined &&
        this.overviewType() === 'overview_suggestion') ||
        (this.overviewType() === 'overview_upload' &&
          this.file() !== undefined)) &&
      !this.overviewAttachedToRecord()
    );
  });

  listOfOverviewTypes = [
    'overview_none',
    'overview_upload',
    'overview_suggestion',
  ];

  api5Configuration = inject(API5_CONFIGURATION);
  apiConfiguration = inject(API_CONFIGURATION);

  recordsApi = computed(() => {
    return new RecordsApi(this.apiConfiguration());
  });
  recordsDataStoreApi = computed(() => {
    return new RecordsApi5(this.api5Configuration());
  });

  overviewType = signal(this.listOfOverviewTypes[0]);

  setType(type: string) {
    if (this.overviewAttachedToRecord()) {
      return;
    }
    this.overviewType.set(type);
  }

  setOverviewFile(event: FileUploadHandlerEvent) {
    for (let i = 0; i < event.files.length; i++) {
      this.file.set(event.files[i]);
    }
  }

  setRecordOverview() {
    if (!this.canSaveOverview()) {
      return;
    }

    const putResourceRequest: PutResourceRequest = {
      metadataUuid: this.uuid(),
      file:
        this.suggestion() !== undefined &&
        this.overviewType() === 'overview_suggestion'
          ? new File([this.suggestion()!], 'overview.png')
          : this.file()!,
    };

    this.recordsDataStoreApi()
      .putResource(putResourceRequest)
      .then(
        response => {
          this.recordsApi()
            .processRecord({
              process: 'thumbnail-add',
              metadataUuid: this.uuid(),
              processParams: {
                thumbnail_url: response.url,
              },
            })
            .then(response => {
              console.log(response);
              this.overviewAttachedToRecord.set(true);
            });
        },
        error => {
          console.log(
            'Error uploading file to metadata: ' + error.response.statusText
          );
        }
      );
  }
}
