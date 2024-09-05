import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class DownloadService {
  downloadAsFile = (
    data: string,
    type = 'text/csv',
    fileName = 'download.csv'
  ): void => {
    if (data === null || data.trim() === '') {
      return;
    }

    const blob = new Blob([data], { type: type });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = fileName;
    a.click();
  };
}
