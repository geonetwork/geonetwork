import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GnDatasetInfo } from 'gapi';

@Injectable({
  providedIn: 'root',
})
export class DataUploadService {
  private serverUrl = 'http://localhost:7979/api/data/analysis';

  private http = inject(HttpClient);

  constructor() {}

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
