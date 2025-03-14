import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnalyticsComponentComponent } from './analytics-component.component';

describe('AnalyticsComponentComponent', () => {
  let component: AnalyticsComponentComponent;
  let fixture: ComponentFixture<AnalyticsComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AnalyticsComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnalyticsComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
