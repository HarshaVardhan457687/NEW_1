import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddProjectCardComponent } from './add-project-card.component';

describe('AddProjectCardComponent', () => {
  let component: AddProjectCardComponent;
  let fixture: ComponentFixture<AddProjectCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddProjectCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddProjectCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
