import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { Avatar } from 'primeng/avatar';
import { API_CONFIGURATION } from 'glib';
import { SiteApi, SettingsListResponse } from 'gapi';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'gn-catalog-avatar',
  imports: [Avatar, RouterLink],
  templateUrl: './catalog-avatar.component.html',
})
export class CatalogAvatarComponent implements OnInit {
  apiConfiguration = inject(API_CONFIGURATION);

  siteApi = computed(() => {
    return new SiteApi(this.apiConfiguration());
  });

  siteInformation = signal<SettingsListResponse | undefined>(undefined);
  siteLogoUrl: string;

  constructor() {}

  ngOnInit(): void {
    this.siteApi()
      .getSiteOrPortalDescription({ headers: { accept: 'application/json' } })
      .then(siteInformation => {
        this.siteInformation.set(siteInformation);
        const nodeId = '';
        this.siteLogoUrl = `${this.apiConfiguration().basePath}/images/logos/ ${nodeId}.png`;
      });
  }
}
