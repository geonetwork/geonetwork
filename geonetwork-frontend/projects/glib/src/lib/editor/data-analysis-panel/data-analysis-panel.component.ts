import {
  Component,
  computed,
  inject,
  input,
  Input,
  linkedSignal,
  signal,
  WritableSignal,
} from '@angular/core';
import { DropdownModule } from 'primeng/dropdown';
import { PrimeTemplate } from 'primeng/api';
import { TableModule } from 'primeng/table';
import {
  AttributeStatistics,
  DataAnalysisApi,
  DateRangeDetails,
  IndexRecord,
  VerticalRange,
} from 'g5api';
import { Select, SelectChangeEvent } from 'primeng/select';
import { FormsModule } from '@angular/forms';
import { API5_CONFIGURATION } from '../../config/config.loader';
import { InplaceFieldComponent } from '../../editor-field/inplace-field/inplace-field.component';
import { TemporalExtentFieldComponent } from '../../editor-field/temporal-extent-field/temporal-extent-field.component';
import { ProgressBar } from 'primeng/progressbar';
import { TemporalExtentService } from '../../editor-field/temporal-extent-field/temporal-extent.service';
import { VerticalExtentFieldComponent } from '../../editor-field/vertical-extent-field/vertical-extent-field.component';
import { JsonPipe } from '@angular/common';
import { VerticalExtentService } from '../../editor-field/vertical-extent-field/vertical-extent.service';

enum StatsType {
  TEMPORAL = 'temporalExtent',
  VERTICAL = 'resourceVerticalRange',
}

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
    VerticalExtentFieldComponent,
    JsonPipe,
  ],
  templateUrl: './data-analysis-panel.component.html',
})
export class DataAnalysisPanelComponent {
  @Input() analysisResult!: WritableSignal<IndexRecord | undefined>;
  @Input() datasource!: WritableSignal<string>;
  @Input() layername!: WritableSignal<string>;

  api5Configuration = inject(API5_CONFIGURATION);
  dataAnalysisApi = computed(() => {
    return new DataAnalysisApi(this.api5Configuration());
  });

  uuid = input.required<string>();

  spatialRepresentationType = computed(
    () => this.analysisResult()?.cl_spatialRepresentationType[0].key
  );
  isVector = computed(() => this.spatialRepresentationType() === 'vector');
  isRaster = computed(() => this.spatialRepresentationType() === 'grid');
  isTabular = computed(() => this.spatialRepresentationType() === 'textTable');

  temporalExtentStats = signal<AttributeStatistics[] | undefined>(undefined);
  temporalExtent = signal<DateRangeDetails | undefined>(undefined);
  resourceVerticalRangeStats = signal<AttributeStatistics[] | undefined>(
    undefined
  );
  resourceVerticalRange = signal<VerticalRange | undefined>(undefined);

  resourceIdentifier = linkedSignal<string>(() => {
    const resolution = this.analysisResult()?.resolutionDistance?.[0];
    const spatialType = this.isVector()
      ? 'v'
      : this.isRaster()
        ? 'r'
        : this.isTabular()
          ? 't'
          : '';
    const coordinateSystem =
      this.analysisResult()?.coordinateSystem?.[0].replace('EPSG:', '') || '';
    const roundedResolution = resolution ? Math.round(Number(resolution)) : '';
    const temporalExtent = this.temporalExtent()
      ? `${this.temporalExtent()?.start?.date || ''}-${this.temporalExtent()?.end?.date || ''}`
      : '';

    return `org_${spatialType}_${coordinateSystem}_${roundedResolution}_m__p_${temporalExtent}_1_0`;
  });

  isComputingStatistics = signal<{ [key in StatsType]: boolean }>({
    [StatsType.TEMPORAL]: false,
    [StatsType.VERTICAL]: false,
  });
  hasComputedStatistics = signal<{ [key in StatsType]: boolean }>({
    [StatsType.TEMPORAL]: false,
    [StatsType.VERTICAL]: false,
  });

  temporalExtentService = inject(TemporalExtentService);
  verticalExtentService = inject(VerticalExtentService);

  getStatistics($event: SelectChangeEvent, type: StatsType) {
    const statsField =
      type === StatsType.TEMPORAL
        ? 'temporalExtentStats'
        : 'resourceVerticalRangeStats';

    this[statsField].set(undefined);
    this.isComputingStatistics.update(prev => {
      return { ...prev, [type]: true };
    });
    this.hasComputedStatistics.update(prev => {
      return { ...prev, [type]: false };
    });
    this.dataAnalysisApi()
      .attributeStatistics({
        datasource: this.datasource(),
        layer: this.layername(),
        attribute: $event.value,
        uuid: this.uuid(),
      })
      .then(
        response => {
          this.isComputingStatistics.update(prev => {
            return { ...prev, [type]: false };
          });
          this.hasComputedStatistics.update(prev => {
            return { ...prev, [type]: true };
          });
          this[statsField].set(response);
          if (type === StatsType.TEMPORAL) {
            this.temporalExtent.set(
              this.temporalExtentService.buildDateRangeDetails(
                String(this.temporalExtentStats()![0].statistics?.MIN),
                String(this.temporalExtentStats()![0].statistics?.MAX)
              )
            );
          } else {
            this.resourceVerticalRange.set(
              this.verticalExtentService.buildVerticalExtent(
                Number(this.resourceVerticalRangeStats()![0].statistics?.MIN),
                Number(this.resourceVerticalRangeStats()![0].statistics?.MAX)
              )
            );
          }
        },
        error => {
          this.isComputingStatistics.update(prev => {
            return { ...prev, [type]: false };
          });
          console.log(
            'Error retrieving attribute statistic : ' + error.response
          );
        }
      );
  }

  protected readonly String = String;
  StatsType = StatsType;
}
