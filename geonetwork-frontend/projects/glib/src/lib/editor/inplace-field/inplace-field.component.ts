import { Component, computed, inject, input, model } from '@angular/core';
import { Button } from 'primeng/button';
import { FormsModule } from '@angular/forms';
import { Inplace } from 'primeng/inplace';
import { InputGroup } from 'primeng/inputgroup';
import { InputText } from 'primeng/inputtext';
import { API5_CONFIGURATION } from 'glib';
import { RecordsApi, BatchEditRequest } from 'g5api';

@Component({
  selector: 'g-inplace-field',
  imports: [Button, FormsModule, Inplace, InputGroup, InputText],
  templateUrl: './inplace-field.component.html',
  styleUrl: './inplace-field.component.css',
})
export class InplaceFieldComponent {
  uuid = input.required<string>();
  property = input.required<string>();
  value = model<string>();

  api5Configuration = inject(API5_CONFIGURATION);

  recordsApi = computed(() => {
    return new RecordsApi(this.api5Configuration());
  });

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
        cb(event);
      });
  }
}
