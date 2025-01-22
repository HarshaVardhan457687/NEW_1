import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoleSelectionPageComponent } from './role-selection-page.component';

describe('RoleSelectionPageComponent', () => {
  let component: RoleSelectionPageComponent;
  let fixture: ComponentFixture<RoleSelectionPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoleSelectionPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoleSelectionPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
