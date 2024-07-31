import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ImageModule } from 'primeng/image';
import { RecordFieldRange } from './record-field-range.pipe';
import { RecordFieldLinksComponent } from './record-field-links/record-field-links.component';
import { RecordFieldKeywordsComponent } from './record-field-keywords/record-field-keywords.component';

@NgModule({
  declarations: [RecordFieldLinksComponent, RecordFieldKeywordsComponent],
  providers: [],
  imports: [CommonModule, ImageModule],
})
export class RecordModule {}
