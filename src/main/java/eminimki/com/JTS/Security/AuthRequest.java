package eminimki.com.JTS.Security;

import lombok.NonNull;

public record AuthRequest(@NonNull String username, @NonNull String password) {
}
