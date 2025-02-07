import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimelineUpdateCardComponent } from './timeline-update-card.component';

describe('TimelineUpdateCardComponent', () => {
  let component: TimelineUpdateCardComponent;
  let fixture: ComponentFixture<TimelineUpdateCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TimelineUpdateCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TimelineUpdateCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
