import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalCardComponent } from './approval-card.component';

describe('ApprovalCardComponent', () => {
  let component: ApprovalCardComponent;
  let fixture: ComponentFixture<ApprovalCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApprovalCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ApprovalCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
