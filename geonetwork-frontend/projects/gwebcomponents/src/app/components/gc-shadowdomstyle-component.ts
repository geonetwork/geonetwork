import {
  AfterViewInit,
  Component,
  ElementRef,
  inject,
  Input,
  OnChanges, OnDestroy,
  OnInit,
  signal,
  SimpleChanges,
  ViewEncapsulation,
} from '@angular/core';
import { Configuration, DefaultConfig } from 'gapi';
import { API5_CONFIGURATION, API_CONFIGURATION, SearchService } from 'glib';
import { Configuration as Gn5Configuration } from 'g5api';

/**
 * Workaround https://github.com/primefaces/primeng/issues/16567
 */
@Component({
  selector: 'gc-shadowdomstyle-component',
  template: '<div></div>',
})
export class GcShadowdomstyleComponentComponent implements OnInit, OnDestroy {
  el = inject(ElementRef);

  loadedStyles: string[] = [];

  observer: MutationObserver;

  ngOnInit(): void {
    this.observer = new MutationObserver(mutations => {
      for (const mutation of mutations) {
        for (const node of Array.from(mutation.addedNodes)) {
          if (
            node instanceof HTMLStyleElement &&
            (node.getAttribute('data-primeng-style-id') !== null ||
              node.textContent?.includes('--p-primary-color'))
          ) {
            this.injectPrimeNGStyles();
            return;
          }
        }
      }
    });
    this.observer.observe(document.head, { childList: true });
  }

  ngOnDestroy() {
    this.observer.disconnect();
  }

  private injectPrimeNGStyles(): void {
    const shadowRoot = this.el.nativeElement?.shadowRoot;
    if (!shadowRoot) {
      return;
    }

    const primeNgDynamicStyles = Array.from(
      document.head.querySelectorAll('style[data-primeng-style-id]')
    );
    const mainThemeStyle = Array.from(
      document.head.querySelectorAll('style')
    ).find(style => style.textContent?.includes('--p-primary-color'));
    const allStyles = [...primeNgDynamicStyles];
    if (mainThemeStyle) allStyles.push(mainThemeStyle);

    allStyles.forEach(styleEl => {
      const styleId = styleEl.getAttribute('data-primeng-style-id') || '';
      if (!styleEl.textContent || this.loadedStyles.includes(styleId)) return;
      const clonedStyle = document.createElement('style');
      clonedStyle.type = 'text/css';
      clonedStyle.setAttribute('data-primeng-style-id', styleId);
      clonedStyle.textContent = styleEl.textContent.replace(/:root/g, ':host');
      shadowRoot.appendChild(clonedStyle);

      this.loadedStyles.push(styleId);
    });
  }
}
