import { DataTableComponent } from './data-table.component'
import {signal} from "@angular/core";

describe('DataTableComponent', () => {
  it('should mount', () => {
    cy.mount(DataTableComponent, {
      source: signal('https://sdi.eea.europa.eu/webdav/datastore/public/coe_t_emerald_p_2021-2022_v05_r00/Emerald_2022_BIOREGION.csv')
    })
  })
})
