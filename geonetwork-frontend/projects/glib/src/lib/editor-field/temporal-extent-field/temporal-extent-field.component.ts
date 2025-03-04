import {
  Component,
  effect,
  inject,
  input,
  OnInit,
  output,
  signal,
} from '@angular/core';
import { DatePicker } from 'primeng/datepicker';
import { FormsModule } from '@angular/forms';
import { Message } from 'primeng/message';
import { DateRangeDetails } from 'g5api';
import { TemporalExtentService } from './temporal-extent.service';

@Component({
  selector: 'g-temporal-extent-field',
  imports: [DatePicker, FormsModule, Message],
  templateUrl: './temporal-extent-field.component.html',
})
export class TemporalExtentFieldComponent implements OnInit {
  from = input<string>();
  to = input<string>();
  format = input('yy');
  appendTo = input<string | HTMLElement>('body');

  onChange = output<DateRangeDetails>();

  calendarRange: string[] | Date[] = [];

  temporalExtentService = inject(TemporalExtentService);

  dateRangeDetails = signal<DateRangeDetails | undefined>(undefined);

  ngOnInit() {
    if (this.temporalExtentService.isValid(this.from(), this.to())) {
      this.calendarRange = this.temporalExtentService.getCalendarRange(
        this.from(),
        this.to()
      );
      this.setValue(this.from(), this.to());
    } else {
      this.errorMessage.set(`Invalid date value ${this.from()}-${this.to()}`);
    }
  }

  errorMessage = signal<string | undefined>(undefined);

  setValue(from: string | undefined, to: string | undefined) {
    this.dateRangeDetails.set(
      this.temporalExtentService.buildDateRangeDetails(from, to)
    );
    this.onChange.emit(this.dateRangeDetails()!);
  }

  onSelect(event: any) {
    if (this.calendarRange[0] && this.calendarRange[1]) {
      this.setValue(
        this.calendarRange[0] as string,
        this.calendarRange[1] as string
      );
    }
  }
}
