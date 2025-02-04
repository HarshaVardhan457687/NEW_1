import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObjectivesPageComponent } from './objectives-page.component';

describe('ObjectivesPageComponent', () => {
  let component: ObjectivesPageComponent;
  let fixture: ComponentFixture<ObjectivesPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ObjectivesPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ObjectivesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
