import { Component, Input, WritableSignal } from '@angular/core';
import { DropdownModule } from 'primeng/dropdown';
import { PrimeTemplate } from 'primeng/api';
import { TableModule } from 'primeng/table';
import { GnDatasetInfo, GnRasterInfo, GnRasterInfoDataTypeEnum } from 'gapi';

@Component({
  selector: 'g-data-analysis-panel',
  standalone: true,
  imports: [DropdownModule, PrimeTemplate, TableModule],
  templateUrl: './data-analysis-panel.component.html',
  styleUrl: './data-analysis-panel.component.css',
})
export class DataAnalysisPanelComponent {
  @Input() analysisResult!: WritableSignal<
    GnDatasetInfo | GnRasterInfo | undefined
  >;

  protected readonly GnRasterInfoDataTypeEnum = GnRasterInfoDataTypeEnum;
}
