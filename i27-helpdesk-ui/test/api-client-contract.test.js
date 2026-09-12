const test = require("node:test");
const assert = require("node:assert/strict");
const fs = require("node:fs");
const path = require("node:path");

const source = fs.readFileSync(path.join(__dirname, "../src/lib/api.ts"), "utf8");

test("creates an Axios client", () => {
  assert.match(source, /axios\.create\(/);
});

test("uses the configured public gateway URL", () => {
  assert.match(source, /baseURL:\s*process\.env\.NEXT_PUBLIC_API_BASE_URL/);
});

test("does not add credentials to login requests", () => {
  assert.match(source, /isLoginRequest/);
  assert.match(source, /!isLoginRequest/);
});

test("forwards the stored bearer token", () => {
  assert.match(source, /localStorage\.getItem\("token"\)/);
  assert.match(source, /Authorization\s*=\s*`Bearer \$\{token\}`/);
});

test("forwards stored user roles", () => {
  assert.match(source, /X-User-Roles/);
  assert.match(source, /user\.roles\.join\(","\)/);
});
