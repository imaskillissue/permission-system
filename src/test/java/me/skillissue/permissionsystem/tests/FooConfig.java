package me.skillissue.permissionsystem.tests;

import me.skillissue.permissionsystem.utils.FileConfigField;

public class FooConfig {
  @FileConfigField public String text = "HALLO HALLO WELT";
  public String not_savable = "I Shouldnt be here";
  @FileConfigField protected int number = Integer.MIN_VALUE;
  @FileConfigField private double wha = Math.PI;

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public double getWha() {
    return wha;
  }

  public void setWha(double wha) {
    this.wha = wha;
  }
}
