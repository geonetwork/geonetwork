import { Component } from '@angular/core';
import {DataUploadComponent} from "glib";

@Component({
  selector: 'gn-data-upload',
  templateUrl: './data-upload-page.component.html',
  standalone: true,
  imports: [
    DataUploadComponent
  ],
  styleUrl: './data-upload-page.component.css'
})
export class DataUploadPageComponent {

}
