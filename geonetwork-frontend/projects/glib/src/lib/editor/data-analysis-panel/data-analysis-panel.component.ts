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
  DateRangeDetails,
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

  temporalExtentStats = signal<AttributeStatistics[] | undefined>(undefined);
  temporalExtent = signal<DateRangeDetails | undefined>(undefined);
  verticalExtent = signal<AttributeStatistics[] | undefined>(undefined);

  isComputingStatistics = signal<boolean>(false);

  getStatistics($event: SelectChangeEvent) {
    this.temporalExtentStats.set(undefined);
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
          this.temporalExtentStats.set(response);
          this.temporalExtent.set({
            start: {
              date: String(this.temporalExtentStats()![0].statistics?.MIN),
            },
            end: {
              date: String(this.temporalExtentStats()![0].statistics?.MAX),
            },
          });
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
