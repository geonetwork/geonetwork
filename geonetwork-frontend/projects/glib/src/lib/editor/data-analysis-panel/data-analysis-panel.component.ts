import {
  Component,
  computed,
  input,
  Input,
  WritableSignal,
} from '@angular/core';
import { DropdownModule } from 'primeng/dropdown';
import { PrimeTemplate } from 'primeng/api';
import { TableModule } from 'primeng/table';
import { GnDatasetInfo, GnRasterInfo, GnRasterInfoDataTypeEnum } from 'gapi';
import { Select } from 'primeng/select';
import { JsonPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InplaceFieldComponent } from '../inplace-field/inplace-field.component';

@Component({
  selector: 'g-data-analysis-panel',
  standalone: true,
  imports: [
    DropdownModule,
    PrimeTemplate,
    TableModule,
    Select,
    JsonPipe,
    FormsModule,
    InplaceFieldComponent,
  ],
  templateUrl: './data-analysis-panel.component.html',
})
export class DataAnalysisPanelComponent {
  @Input() analysisResult!: WritableSignal<
    GnDatasetInfo | GnRasterInfo | undefined
  >;

  uuid = input.required<string>();

  resourceIdentifier = '';

  isVector = (
    datasetInfo: GnDatasetInfo | GnRasterInfo | undefined
  ): datasetInfo is GnDatasetInfo => {
    return true;
  };
  isRaster = (
    datasetInfo: GnDatasetInfo | GnRasterInfo | undefined
  ): datasetInfo is GnRasterInfo => {
    return true;
  };

  crsDefinition = computed(() => {
    let output: string | undefined;
    let datasetInfo = this.analysisResult();
    if (
      this.isVector(datasetInfo) &&
      datasetInfo?.layers &&
      datasetInfo.layers[0].geometryFields &&
      datasetInfo.layers[0].geometryFields.length > 0
    ) {
      output = datasetInfo.layers[0].geometryFields[0].crs;
    } else if (this.isRaster(datasetInfo)) {
      output = datasetInfo?.crs;
    }
    return output;
  });
  crsCode = computed(() => {
    return this.crsDefinition()
      ? this.crsDefinition()?.replace(/[\s\S.]*"EPSG",([0-9]*).*/, '$1')
      : '';
  });

  protected readonly GnRasterInfoDataTypeEnum = GnRasterInfoDataTypeEnum;
}
