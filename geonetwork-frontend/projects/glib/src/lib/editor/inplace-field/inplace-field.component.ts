import {
  Component,
  computed,
  inject,
  input,
  model,
  OnInit,
  signal,
  WritableSignal,
} from '@angular/core';
import { Button } from 'primeng/button';
import { FormsModule } from '@angular/forms';
import { Inplace } from 'primeng/inplace';
import { InputGroup } from 'primeng/inputgroup';
import { InputText } from 'primeng/inputtext';
import { API5_CONFIGURATION } from 'glib';
import { BatchEditRequest, RecordsApi } from 'g5api';
import { Tooltip } from 'primeng/tooltip';
import { Select } from 'primeng/select';
import { InputGroupAddon } from 'primeng/inputgroupaddon';
import { InputNumber } from 'primeng/inputnumber';
import { DatePicker } from 'primeng/datepicker';
import { EeaResourceIdentifierFieldComponent } from '../eea-resource-identifier-field/eea-resource-identifier-field.component';

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

  api5Configuration = inject(API5_CONFIGURATION);

  recordsApi = computed(() => {
    return new RecordsApi(this.api5Configuration());
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
        cb(event);
      });
  }

  cancelEdit(event: Event, cb: Function) {
    this.value.set(this.initialValue);
    cb(event);
  }
}
