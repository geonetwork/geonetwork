import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class DataAnalysisService {
  private readonly datasourceFilePrefix = 'local://';

  isLocalDatasource(datasource: string): boolean {
    return datasource.startsWith(this.datasourceFilePrefix);
  }

  getLocalDatasourceString(datasource: string) {
    return this.datasourceFilePrefix + datasource;
  }

  buildDatasourceParameter(datasource: string): string {
    return this.isLocalDatasource(datasource)
      ? datasource.slice(this.datasourceFilePrefix.length)
      : datasource;
  }

  constructor() {}
}
