#!/usr/bin/env bash
set -euo pipefail

echo "==> Removendo pacotes via APT..."
sudo apt purge -y nvtop btop easyeffects gnome-system-monitor meld timeshift timeshift-gtk 2>/dev/null || true

echo "==> Verificando Snap..."
for pkg in bruno timeshift meld btop; do
    if snap list 2>/dev/null | grep -qi "^$pkg "; then
        sudo snap remove --purge "$pkg"
    fi
done

echo "==> Verificando Flatpak..."
for pkg in com.usebruno.Bruno io.github.celluloid_player.Celluloid org.gnome.SystemMonitor com.github.wwmm.easyeffects org.gnome.meld com.github.teejee2008.timeshift; do
    if flatpak list 2>/dev/null | grep -qi "$pkg"; then
        flatpak uninstall -y "$pkg"
    fi
done

echo "==> Removendo Bruno (AppImage/.deb manual, se existir)..."
sudo rm -f /usr/bin/bruno /opt/Bruno*.AppImage 2>/dev/null || true
sudo rm -rf /opt/Bruno 2>/dev/null || true
rm -f "$HOME/Applications/Bruno"*.AppImage 2>/dev/null || true
rm -f "$HOME/.local/share/applications/bruno.desktop" 2>/dev/null || true

echo "==> Limpando configs e cache residuais..."
rm -rf "$HOME/.config/nvtop" \
       "$HOME/.config/btop" "$HOME/.cache/btop" \
       "$HOME/.config/easyeffects" "$HOME/.cache/easyeffects" \
       "$HOME/.config/timeshift" "$HOME/.cache/timeshift" \
       "$HOME/.config/meld" "$HOME/.cache/meld" \
       "$HOME/.config/usebruno" "$HOME/.cache/usebruno" \
       "$HOME/.config/bruno" "$HOME/.cache/bruno" \
       2>/dev/null || true

sudo rm -rf /etc/timeshift /var/log/timeshift* 2>/dev/null || true

echo "==> Autoremove + autoclean APT..."
sudo apt autoremove -y
sudo apt autoclean -y
sudo apt clean

echo "==> Limpando cache snap antigo (se usar snap)..."
if command -v snap >/dev/null 2>&1; then
    sudo snap set system refresh.retain=2 2>/dev/null || true
fi

echo "Concluído."
