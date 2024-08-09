import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ImageModule } from 'primeng/image';
import { RecordFieldRange } from './record-field-range.pipe';
import { RecordFieldLinksComponent } from './record-field-links/record-field-links.component';
import { RecordFieldKeywordsComponent } from './record-field-keywords/record-field-keywords.component';
import { RecordViewComponent } from './record-view/record-view.component';

@NgModule({
  declarations: [
    RecordFieldLinksComponent,
    RecordFieldKeywordsComponent,
    RecordViewComponent,
  ],
  providers: [],
  imports: [CommonModule, ImageModule],
})
export class RecordModule {}
