import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DataTableComponent } from './data-table/data-table.component';
import {TableModule} from "primeng/table";



@NgModule({
  declarations: [],
  imports: [CommonModule, TableModule],
})
export class DataModule {}
