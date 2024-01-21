import { Injectable } from '@angular/core';
import { LoginData } from '../models/user';
import { AES, enc } from 'crypto-js'
import { secretKey } from '../utils/constants';

@Injectable({
  providedIn: 'root',
})
export class RememberMeService {
  private readonly rememberMeKey = 'rememberMeData';

  storeRememberMeData(userData: LoginData): void {
    const encryptedData = AES.encrypt(JSON.stringify(userData), secretKey).toString(); 
    localStorage.setItem(this.rememberMeKey, encryptedData);
  }

  getRememberMeData(): LoginData | null {
    const encryptedData = localStorage.getItem(this.rememberMeKey);

    if (encryptedData) {
      const decryptedData = AES.decrypt(encryptedData, secretKey).toString(enc.Utf8);
      
      if (decryptedData) {
        const rememberMeData: LoginData = JSON.parse(decryptedData);
        return rememberMeData;
      }
    }

    return null;
  }

  clearRememberMeData(): void {
    localStorage.removeItem(this.rememberMeKey);
    return;
  }
}
