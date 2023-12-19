package me.skillissue.permissionsystem.tests;

import me.skillissue.permissionsystem.utils.FileConfigField;

public class FooConfig {
 @FileConfigField public String text = "HALLO HALLO WELT";
 @FileConfigField protected int number = Integer.MIN_VALUE;
 public String not_savable = "I Shouldnt be here";
 @FileConfigField private double wha = Math.PI;

 public int getNumber() {
  return number;
 }

 public double getWha() {
  return wha;
 }

 public void setNumber(int number) {
  this.number = number;
 }

 public void setWha(double wha) {
  this.wha = wha;
 }
}
