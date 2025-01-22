import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgressBarLinearComponent } from './progress-bar-linear.component';

describe('ProgressBarLinearComponent', () => {
  let component: ProgressBarLinearComponent;
  let fixture: ComponentFixture<ProgressBarLinearComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProgressBarLinearComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProgressBarLinearComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
