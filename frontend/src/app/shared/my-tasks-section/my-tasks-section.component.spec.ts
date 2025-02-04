import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyTasksSectionComponent } from './my-tasks-section.component';

describe('MyTasksSectionComponent', () => {
  let component: MyTasksSectionComponent;
  let fixture: ComponentFixture<MyTasksSectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyTasksSectionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyTasksSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
