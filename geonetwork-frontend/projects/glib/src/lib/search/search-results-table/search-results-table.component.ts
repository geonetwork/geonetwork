import { Component, computed, effect, input } from '@angular/core';
import { SearchBaseComponent } from '../search-base/search-base.component';
import { TableModule, TablePageEvent } from 'primeng/table';
import { Button } from 'primeng/button';
import { JsonPipe } from '@angular/common';
import { GJsonpathPipe } from '../../shared/g-jsonpath.pipe';

@Component({
  selector: 'g-search-results-table',
  templateUrl: './search-results-table.component.html',
  styleUrl: './search-results-table.component.css',
  standalone: true,
  imports: [TableModule, Button, JsonPipe, GJsonpathPipe],
})
export class SearchResultsTableComponent extends SearchBaseComponent {
  fields = input<string[]>(); // fields to display as table mode
  selectionMode = input<'single' | 'multiple' | undefined>();

  selectedRecords: any[] = [];
  constructor() {
    super();
    effect(() => {
      this.selectionMode();
      this.selectedRecords =  [];
    });
  }

  pageChange(event: TablePageEvent) {
    console.log(event);
    this.search.setPage(event.first, event.rows);
  }
}
