import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyTaskCardComponent } from './my-task-card.component';

describe('MyTaskCardComponent', () => {
  let component: MyTaskCardComponent;
  let fixture: ComponentFixture<MyTaskCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyTaskCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyTaskCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
