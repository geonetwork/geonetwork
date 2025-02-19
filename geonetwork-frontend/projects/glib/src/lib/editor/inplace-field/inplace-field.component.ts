import {
  Component,
  computed,
  inject,
  input,
  model,
  OnInit,
} from '@angular/core';
import { Button } from 'primeng/button';
import { FormsModule } from '@angular/forms';
import { Inplace } from 'primeng/inplace';
import { InputText } from 'primeng/inputtext';
import { BatchEditRequest, RecordsApi } from 'g5api';
import { RecordsApi as Gn4RecordsApi } from 'gapi';
import { EeaResourceIdentifierFieldComponent } from '../eea-resource-identifier-field/eea-resource-identifier-field.component';
import {
  API5_CONFIGURATION,
  API_CONFIGURATION,
} from '../../config/config.loader';

@Component({
  selector: 'g-inplace-field',
  imports: [
    Button,
    FormsModule,
    Inplace,
    InputText,
    EeaResourceIdentifierFieldComponent,
  ],
  templateUrl: './inplace-field.component.html',
  styleUrl: './inplace-field.component.css',
})
export class InplaceFieldComponent implements OnInit {
  uuid = input.required<string>();
  property = input.required<string>();
  value = model<string>();
  initialValue: string | undefined;

  apiConfiguration = inject(API_CONFIGURATION);
  api5Configuration = inject(API5_CONFIGURATION);

  recordsApi = computed(() => {
    return new RecordsApi(this.api5Configuration());
  });
  gn4RecordsApi = computed(() => {
    return new Gn4RecordsApi(this.apiConfiguration());
  });

  ngOnInit(): void {
    this.initialValue = this.value();
  }

  saveEdit(event: Event, cb: Function) {
    let batchEditRequest: BatchEditRequest = {
      uuids: [this.uuid()],
      batchEditParameter: [
        {
          property: this.property(),
          value: this.value() || '',
        },
      ],
    };
    this.recordsApi()
      .batchEdit(batchEditRequest)
      .then(response => {
        this.gn4RecordsApi().index({ uuids: [this.uuid()] });
        cb(event);
      });
  }

  cancelEdit(event: Event, cb: Function) {
    this.value.set(this.initialValue);
    cb(event);
  }
}
