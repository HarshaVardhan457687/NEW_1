import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalsSectionComponent } from './approvals-section.component';

describe('ApprovalsSectionComponent', () => {
  let component: ApprovalsSectionComponent;
  let fixture: ComponentFixture<ApprovalsSectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ApprovalsSectionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ApprovalsSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
