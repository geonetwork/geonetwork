import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { RecordViewComponent } from 'glib';

@Component({
  selector: 'gn-record-page',
  templateUrl: './record-page.component.html',
  standalone: true,
  styleUrl: './record-page.component.css',
  imports: [RecordViewComponent, RouterLink],
})
export class RecordPageComponent implements OnInit {
  router = inject(Router);
  route = inject(ActivatedRoute);
  uuid = signal<string | undefined>(undefined);

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.uuid.set(params.uuid);
    });
  }
}
