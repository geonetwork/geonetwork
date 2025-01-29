import {
  Component,
  computed,
  effect,
  model,
  OnInit,
  signal,
  WritableSignal,
} from '@angular/core';
import { DatePicker } from 'primeng/datepicker';
import { FormsModule } from '@angular/forms';
import { InputGroupAddon } from 'primeng/inputgroupaddon';
import { InputNumber } from 'primeng/inputnumber';
import { InputText } from 'primeng/inputtext';
import { Select } from 'primeng/select';
import { Tooltip } from 'primeng/tooltip';
import { InputGroup } from 'primeng/inputgroup';

export interface EEAResourceIdentifier {
  provider: WritableSignal<string>;
  dataType: WritableSignal<string>;
  epsg: WritableSignal<number | undefined>;
  scale: WritableSignal<number | undefined>;
  scaleUnit: WritableSignal<string | undefined>;
  shortName: WritableSignal<string>;
  access: WritableSignal<string>;
  temporalCoverage: WritableSignal<Date[] | undefined>;
  version: WritableSignal<number | undefined>;
  revision: WritableSignal<number | undefined>;
}

@Component({
  selector: 'g-eea-resource-identifier-field',
  imports: [
    DatePicker,
    FormsModule,
    InputGroupAddon,
    InputNumber,
    InputText,
    Select,
    Tooltip,
    InputGroup,
  ],
  templateUrl: './eea-resource-identifier-field.component.html',
  styleUrl: './eea-resource-identifier-field.component.css',
})
export class EeaResourceIdentifierFieldComponent implements OnInit {
  value = model.required<string>();

  resourceIdentifier: EEAResourceIdentifier = {
    provider: signal('eea'),
    dataType: signal(''),
    epsg: signal(undefined),
    scale: signal(undefined),
    scaleUnit: signal(undefined),
    shortName: signal(''),
    access: signal(''),
    temporalCoverage: signal(undefined),
    version: signal(1),
    revision: signal(0),
  };

  constructor() {
    effect(() => {
      this.value.set(
        [
          this.resourceIdentifier!.provider(),
          this.resourceIdentifier!.dataType(),
          this.resourceIdentifier!.epsg(),
          this.resourceIdentifier!.scale(),
          this.resourceIdentifier!.scaleUnit(),
          this.resourceIdentifier!.shortName(),
          this.resourceIdentifier!.access(),
          this.resourceIdentifier!.temporalCoverage()
            ?.filter((date: Date) => {
              return date !== null;
            })
            .join('-'),
          'v' + this.resourceIdentifier!.version(),
          'r' + this.resourceIdentifier!.revision(),
        ].join('_')
      );
    });
  }

  ngOnInit(): void {
    if (this.value()) {
      let tokens = this.value().split('_');
      this.resourceIdentifier.provider.set(tokens[0]);
      this.resourceIdentifier.dataType.set(tokens[1]);
      this.resourceIdentifier.epsg.set(parseInt(tokens[2]));
      this.resourceIdentifier.scale.set(parseInt(tokens[3]));
      this.resourceIdentifier.scaleUnit.set(tokens[4]);
      this.resourceIdentifier.shortName.set(tokens[5]);
      this.resourceIdentifier.access.set(tokens[6]);
      this.resourceIdentifier.temporalCoverage.set(
        tokens[7].split('-').map(date => new Date(date))
      );
      this.resourceIdentifier.version.set(
        parseInt(tokens[8].substring(1)) || 1
      );
      this.resourceIdentifier.revision.set(
        parseInt(tokens[9].substring(1)) || 0
      );
    }
  }

  eeaDataTypes = [
    { code: 'r', name: 'Raster datasets (GeoTIFF, Erdas IMG, DEMs)' },
    {
      code: 'v',
      name: 'Vectorial datasets (GML, Shapefile, GDB, Ascii Coordinate data)',
    },
    { code: 't', name: 'Tabular datasets (csv, xlsx, accdb)' },
    { code: 's', name: 'Series' },
  ];

  eeaDataAccessTypes = [
    {
      code: 'p',
      name: 'Public',
      title:
        'Datasets available for download through the EEA website\n' +
        'Public versions of EEA datasets (not necessarily available through the website but explicitly indicated in the use limitation/resource constraints)',
    },
    {
      code: 'i',
      name: 'Internal',
      title:
        'Any kind of restricted datasets shared with the EEA (GISCO, EuroGeographics)\n' +
        'Internal versions of EEA datasets\n' +
        'Default option when we do not know about the data sharing policy',
    },
  ];

  eeaScaleResolutionUnits = [
    { code: 'arcsec', name: 'Only with resolution(distance)' },
    { code: 'arcmin', name: 'Only with resolution(distance)' },
    { code: 'k', name: 'Only with scale denominator' },
    { code: 'km', name: 'Only with resolution (distance)' },
    { code: 'm', name: 'Only with resolution (distance)' },
    { code: 'mio', name: 'Only with scale denominator' },
  ];
}
