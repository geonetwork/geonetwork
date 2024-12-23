import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { RecordViewComponent } from 'glib';
import { Location } from '@angular/common';

@Component({
  selector: 'gn-record-page',
  templateUrl: './record-page.component.html',
  standalone: true,
  imports: [RecordViewComponent],
})
export class RecordPageComponent implements OnInit {
  router = inject(Router);
  route = inject(ActivatedRoute);
  uuid = signal<string | undefined>(undefined);

  location = inject(Location);

  back() {
    this.location.back();
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.uuid.set(params.uuid);
    });
  }
}
