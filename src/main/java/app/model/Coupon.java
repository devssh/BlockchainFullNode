package app.model;

/**
 * Created by shilpak on 08/06/18.
 */

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(of= {"product", "type", "discount", "mails"})
public class Coupon {
  public final String product;
  public final String type;
  public final String discount;
  public final String[] mails;

  public Coupon(String product, String discount, String[] mails, String type) {
    this.product = product;
    this.type = type;
    this.discount = discount;
    this.mails = mails;
  }
}
