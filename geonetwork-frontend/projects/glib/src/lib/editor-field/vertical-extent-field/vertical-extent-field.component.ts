import {
  Component,
  inject,
  input,
  OnInit,
  output,
  signal,
} from '@angular/core';
import { Slider } from 'primeng/slider';
import { FormsModule } from '@angular/forms';
import { InputNumber } from 'primeng/inputnumber';
import { VerticalRange } from 'g5api';
import { TemporalExtentService } from '../temporal-extent-field/temporal-extent.service';
import { VerticalExtentService } from './vertical-extent.service';

@Component({
  selector: 'g-vertical-extent-field',
  imports: [Slider, FormsModule, InputNumber],
  templateUrl: './vertical-extent-field.component.html',
})
export class VerticalExtentFieldComponent implements OnInit {
  gte = input<number>(0);
  lte = input<number>(1000);

  onChange = output<VerticalRange>();

  unit = ' m';

  min = signal<number>(0);
  max = signal<number>(0);

  rangeValues: number[];

  verticatlExtentService = inject(VerticalExtentService);

  ngOnInit() {
    this.rangeValues = [this.gte(), this.lte()];
    this.min.set(this.gte());
    this.max.set(this.lte());
  }

  updateRange() {
    this.rangeValues = [...this.rangeValues];
    this.min.set(this.rangeValues[0]);
    this.max.set(this.rangeValues[1]);
    this.emitOnChange();
  }

  emitOnChange() {
    this.onChange.emit(
      this.verticatlExtentService.buildVerticalExtent(
        this.rangeValues[0],
        this.rangeValues[1]
      )
    );
  }
}
