import { Component, effect, input, OnInit } from '@angular/core';
import Papa from 'papaparse';
import { TableModule } from 'primeng/table';

@Component({
  selector: 'g-data-table',
  templateUrl: './data-table.component.html',
  styleUrl: './data-table.component.css',
  standalone: true,
  imports: [TableModule],
})
export class DataTableComponent implements OnInit {
  source = input.required<string>();

  data: Array<any> = [];
  isLoading = false;
  headerData: Array<any> = [];

  constructor() {
    effect(() => {
      this.parseCsv(this.source());
    });
  }

  parseCsv(source: string) {
    this.isLoading = true;
    Papa.parse<Array<any>>(source, {
      download: true,
      // header: true,
      // dynamicTyping: true,
      complete: (results, file) => {
        let data: any[] = [];
        this.headerData = [];
        if (results.errors.length) {
          console.error('Errors while parsing:', results.errors);
        } else {
          this.headerData = results.data[0];
          results.data.slice(1).forEach((row, index) => {
            let object: any = {};
            row.map((value, index) => {
              object[this.headerData[index]] = value;
            });
            data.push(object);
          });
          this.data = data;
        }
        this.isLoading = false;
      },
    });
  }

  ngOnInit() {}
}
