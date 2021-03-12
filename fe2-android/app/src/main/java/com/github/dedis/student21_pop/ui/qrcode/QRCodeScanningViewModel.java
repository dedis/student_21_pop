package com.github.dedis.student21_pop.ui.qrcode;

import com.github.dedis.student21_pop.utility.qrcode.QRCodeListener;

public interface QRCodeScanningViewModel extends QRCodeListener {

  int getScanDescription();
}
