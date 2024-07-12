import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GlibComponent } from './glib.component';

describe('GlibComponent', () => {
  let component: GlibComponent;
  let fixture: ComponentFixture<GlibComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GlibComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(GlibComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
