import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GnDatasetInfo } from 'gapi';

export interface DatasetFormat {
  name: string;
  description: string;
  dataType: string;
  rwFlag: string;
}

@Injectable({
  providedIn: 'root',
})
export class DataUploadService {
  // TODO: Create OpenAPI client for GN5
  private serverUrl = '/api/data/analysis';

  private http = inject(HttpClient);

  constructor() {}

  getFormats(): Observable<DatasetFormat[]> {
    return this.http.get<DatasetFormat[]>(`${this.serverUrl}/formats`);
  }

  previewAnalysis(
    uuid: string,
    datasource: string,
    layer: string
  ): Observable<string> {
    return this.http.get(
      `${this.serverUrl}/preview?uuid=${uuid}&datasource=${datasource}&layer=${layer}`,
      { responseType: 'text' }
    );
  }
  applyAnalysis(
    uuid: string,
    datasource: string,
    layer: string
  ): Observable<any> {
    return this.http.post(
      `${this.serverUrl}/save?uuid=${uuid}&datasource=${datasource}&layer=${layer}`,
      { responseType: 'text' }
    );
  }

  executeAnalysis(
    datasource: string,
    layer: string
  ): Observable<GnDatasetInfo> {
    return this.http.get<GnDatasetInfo>(
      `${this.serverUrl}/execute?datasource=${datasource}&layer=${layer}`
    );
  }

  retrieveLayers(datasource: string): Observable<string[]> {
    return this.http.get<string[]>(
      `${this.serverUrl}/layers?datasource=${datasource}`
    );
  }
}
