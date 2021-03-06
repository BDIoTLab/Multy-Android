/*
 * Copyright 2018 Idealnaya rabota LLC
 * Licensed under Multy.io license.
 * See LICENSE for details
 */

package io.multy.model.responses;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.samwolfand.oneprefs.Prefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.multy.model.entities.wallet.Wallet;
import io.multy.util.Constants;
import io.multy.util.NativeDataHelper;
import io.multy.util.WalletDeserializer;

public class WalletsResponse {

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @JsonAdapter(WalletDeserializer.class)
    @SerializedName("wallets")
    private List<Wallet> wallets;
    @SerializedName("topindexes")
    private ArrayList<TopIndex> topIndexes;

    public ArrayList<TopIndex> getTopIndexes() {
        return topIndexes;
    }

    public void saveTopIndexes() {
        if (topIndexes != null) {
            for (TopIndex index : topIndexes) {
                try {
                    String prefKey = null;
                    switch (NativeDataHelper.Blockchain.valueOf(index.currencyId)) {
                        case BTC:
                            prefKey = Constants.PREF_WALLET_TOP_INDEX_BTC;
                            break;
                        case ETH:
                            prefKey = Constants.PREF_WALLET_TOP_INDEX_ETH;
                            break;
                        case EOS:
                            prefKey = Constants.PREF_WALLET_TOP_INDEX_EOS;
                            break;
                    }
                    Prefs.putInt(prefKey + index.getNetworkId(), index.getTopWalletIndex());
                } catch (Exception e) {
                    e.printStackTrace(); // except if server will send top index of unsupported chain id
                }
            }
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Wallet> getWallets() {
        if (wallets != null) {
            Collections.sort(wallets, (o1, o2) -> Long.compare(o1.getLastActionTime(), o2.getLastActionTime()));
        }
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    public class TopIndex {

        @SerializedName("currencyid")
        private int currencyId;
        @SerializedName("networkid")
        private int networkId;
        @SerializedName("topindex")
        private int topWalletIndex;

        public int getCurrencyId() {
            return currencyId;
        }

        public int getTopWalletIndex() {
            return topWalletIndex;
        }

        public int getNetworkId() {
            return networkId;
        }
    }
}
