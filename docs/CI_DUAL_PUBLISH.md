# Dual-publish (GH Packages + Yuri + JSR)

Org epic: [KolektivComputer/.github#2](https://github.com/KolektivComputer/.github/issues/2)  
Spine: [gradle-conventions#1](https://github.com/KolektivComputer/gradle-conventions/pull/1) → plugin `computer.kolektiv.publishing`

## Maven
- GroupId: `computer.kolektiv.kraft` (org family `computer.kolektiv.*`)
- Dual repositories: GitHub Packages `https://maven.pkg.github.com/KolektivComputer/kraft` **and** Yuri Capital (`maven-releases` / `maven-snapshots`) when credentials are present
- Prefer `id("computer.kolektiv.publishing")` once the conventions plugin is published; until then repos are inlined to match that plugin

## npm
- Scope **`@kolektiv/...` only** — do not invent a second JS scope
- Dual: existing Yuri Capital npm (where used) + `https://npm.pkg.github.com` with `@kolektiv:registry=…`
- Auth: `NODE_AUTH_TOKEN: ${{ secrets.GITHUB_TOKEN }}` with `permissions.packages: write`

## JSR
- Scope `@kolektiv` (owned)
- Actions: OIDC only — `permissions.id-token: write` — **no org `JSR_TOKEN`**
- Mey: link each package on jsr.io for GitHub Actions trusted publishing
- Not a substitute for npm publish

## Workflow paste (token may lack `workflows` scope)

```yaml
permissions:
  contents: read
  packages: write
  id-token: write  # JSR OIDC
```

Maven job env:
```yaml
GITHUB_ACTOR: ${{ github.actor }}
GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
# optional Yuri dual:
# YURI_CAPITAL_REPO_USERNAME / YURI_CAPITAL_REPO_PASSWORD (org secrets)
```

npm → GitHub Packages:
```yaml
- uses: actions/setup-node@v4
  with:
    registry-url: https://npm.pkg.github.com
    scope: "@kolektiv"
env:
  NODE_AUTH_TOKEN: ${{ secrets.GITHUB_TOKEN }}
```

JSR:
```yaml
- run: npx jsr publish
```

Kotlin source packages remain `sh.illumi.kraft.*` until a follow-up rename; Maven group is `computer.kolektiv.kraft`.
