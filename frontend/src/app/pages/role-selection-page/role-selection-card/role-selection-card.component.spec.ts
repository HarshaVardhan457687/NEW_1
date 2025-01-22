import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoleSelectionCardComponent } from './role-selection-card.component';

describe('RoleSelectionCardComponent', () => {
  let component: RoleSelectionCardComponent;
  let fixture: ComponentFixture<RoleSelectionCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoleSelectionCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoleSelectionCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
