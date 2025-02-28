import {
  Component,
  computed,
  inject,
  input,
  Input,
  signal,
  WritableSignal,
} from '@angular/core';
import { DropdownModule } from 'primeng/dropdown';
import { PrimeTemplate } from 'primeng/api';
import { TableModule } from 'primeng/table';
import {
  AttributeStatistics,
  DataAnalysisControllerApi,
  IndexRecord,
} from 'g5api';
import { Select, SelectChangeEvent } from 'primeng/select';
import { FormsModule } from '@angular/forms';
import { JsonPipe } from '@angular/common';
import {
  API5_CONFIGURATION,
  API_CONFIGURATION,
} from '../../config/config.loader';
import { InplaceFieldComponent } from '../../editor-field/inplace-field/inplace-field.component';
import { TemporalExtentFieldComponent } from '../../editor-field/temporal-extent-field/temporal-extent-field.component';
import { ProgressBar } from 'primeng/progressbar';

@Component({
  selector: 'g-data-analysis-panel',
  standalone: true,
  imports: [
    DropdownModule,
    PrimeTemplate,
    TableModule,
    Select,
    FormsModule,
    InplaceFieldComponent,
    TemporalExtentFieldComponent,
    ProgressBar,
  ],
  templateUrl: './data-analysis-panel.component.html',
})
export class DataAnalysisPanelComponent {
  @Input() analysisResult!: WritableSignal<IndexRecord | undefined>;
  @Input() datasource!: WritableSignal<string>;
  @Input() layername!: WritableSignal<string>;

  api5Configuration = inject(API5_CONFIGURATION);
  dataAnalysisApi = computed(() => {
    return new DataAnalysisControllerApi(this.api5Configuration());
  });

  uuid = input.required<string>();

  resourceIdentifier = '';

  temporalExtent = signal<AttributeStatistics[] | undefined>(undefined);
  verticalExtent = signal<AttributeStatistics[] | undefined>(undefined);

  isComputingStatistics = signal<boolean>(false);

  getStatistics($event: SelectChangeEvent) {
    this.temporalExtent.set(undefined);
    this.isComputingStatistics.set(true);
    this.dataAnalysisApi()
      .attributeStatistics({
        datasource: this.datasource(),
        layer: this.layername(),
        attribute: $event.value,
      })
      .then(
        response => {
          this.isComputingStatistics.set(false);
          this.temporalExtent.set(response);
        },
        error => {
          this.isComputingStatistics.set(false);
          console.log(
            'Error retrieving attribute statistic : ' + error.response
          );
        }
      );
  }

  protected readonly String = String;
}
