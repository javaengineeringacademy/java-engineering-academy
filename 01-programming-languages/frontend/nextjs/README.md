# Next.js

Next.js is a React framework for production, providing hybrid static & server rendering, TypeScript support, smart bundling, route pre-fetching, and more with zero configuration.

## Table of Contents

- [App Router vs Pages Router](#app-router-vs-pages-router)
- [Server Components](#server-components)
- [Client Components](#client-components)
- [Layouts](#layouts)
- [Loading States](#loading-states)
- [Error Handling](#error-handling)
- [API Routes](#api-routes)
- [Middleware](#middleware)
- [ISR, SSR, SSG](#isr-ssr-ssg)
- [Image Optimization](#image-optimization)
- [Metadata API](#metadata-api)

---

## App Router vs Pages Router

### Pages Router (Legacy)

The traditional file-based routing using the `pages/` directory:

```typescript
// pages/index.tsx
export default function Home() {
  return <h1>Welcome to Next.js</h1>;
}

// pages/about.tsx
export default function About() {
  return <h1>About Us</h1>;
}

// pages/blog/[slug].tsx
import { useRouter } from "next/router";

export default function BlogPost() {
  const router = useRouter();
  const { slug } = router.query;
  return <h1>Blog Post: {slug}</h1>;
}

// pages/api/hello.ts
import type { NextApiRequest, NextApiResponse } from "next";

export default function handler(req: NextApiRequest, res: NextApiResponse) {
  res.status(200).json({ message: "Hello World" });
}
```

### App Router (Recommended)

The modern file-based routing using the `app/` directory:

```typescript
// app/page.tsx (root page)
export default function Home() {
  return <h1>Welcome to Next.js</h1>;
}

// app/about/page.tsx
export default function About() {
  return <h1>About Us</h1>;
}

// app/blog/[slug]/page.tsx
export default function BlogPost({ params }: { params: { slug: string } }) {
  return <h1>Blog Post: {params.slug}</h1>;
}

// app/api/hello/route.ts
import { NextResponse } from "next/server";

export async function GET() {
  return NextResponse.json({ message: "Hello World" });
}
```

### Key Differences

| Feature | Pages Router | App Router |
|---------|-------------|------------|
| Directory | `pages/` | `app/` |
| Layouts | Custom `_app.tsx` | Built-in `layout.tsx` |
| Loading | Manual | Built-in `loading.tsx` |
| Error | Custom `_error.tsx` | Built-in `error.tsx` |
| Data fetching | `getServerSideProps` | Async components |
| SEO | Manual | Automatic |

---

## Server Components

Server Components run on the server and send rendered HTML to the client:

```typescript
// app/posts/page.tsx (Server Component by default)
async function getPosts() {
  const res = await fetch("https://api.example.com/posts");
  return res.json();
}

export default async function PostsPage() {
  const posts = await getPosts();

  return (
    <div>
      <h1>Posts</h1>
      {posts.map((post) => (
        <article key={post.id}>
          <h2>{post.title}</h2>
          <p>{post.body}</p>
        </article>
      ))}
    </div>
  );
}
```

### Benefits

- Zero client-side JavaScript
- Direct database access
- Automatic code splitting
- Streaming with Suspense
- Reduced bundle size

---

## Client Components

Client Components run on the client and support interactivity:

```typescript
"use client";

import { useState } from "react";

export function Counter() {
  const [count, setCount] = useState(0);

  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={() => setCount(count + 1)}>Increment</button>
      <button onClick={() => setCount(count - 1)}>Decrement</button>
    </div>
  );
}
```

### When to Use Client Components

- Event handlers (`onClick`, `onChange`)
- useState and useEffect
- Browser APIs (`window`, `document`)
- Third-party libraries requiring client-side code
- Custom hooks

---

## Layouts

Layouts wrap pages and persist across navigations:

```typescript
// app/layout.tsx (Root Layout)
import "./globals.css";

export const metadata = {
  title: "My App",
  description: "Built with Next.js",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>
        <header>
          <nav>
            <a href="/">Home</a>
            <a href="/about">About</a>
          </nav>
        </header>
        <main>{children}</main>
        <footer>© 2024 My App</footer>
      </body>
    </html>
  );
}

// app/dashboard/layout.tsx (Nested Layout)
export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <div className="dashboard">
      <aside>
        <nav>
          <a href="/dashboard">Overview</a>
          <a href="/dashboard/settings">Settings</a>
        </nav>
      </aside>
      <section>{children}</section>
    </div>
  );
}
```

---

## Loading States

Built-in loading states using `loading.tsx`:

```typescript
// app/posts/loading.tsx
export default function Loading() {
  return (
    <div className="animate-pulse">
      <div className="h-8 bg-gray-200 rounded w-1/4 mb-4" />
      <div className="h-4 bg-gray-200 rounded w-3/4 mb-2" />
      <div className="h-4 bg-gray-200 rounded w-1/2" />
    </div>
  );
}

// Using Suspense for granular loading
import { Suspense } from "react";

export default function Dashboard() {
  return (
    <div>
      <h1>Dashboard</h1>
      <Suspense fallback={<PostsSkeleton />}>
        <Posts />
      </Suspense>
      <Suspense fallback={<CommentsSkeleton />}>
        <Comments />
      </Suspense>
    </div>
  );
}
```

---

## Error Handling

Built-in error boundaries using `error.tsx`:

```typescript
// app/error.tsx
"use client";

export default function Error({
  error,
  reset,
}: {
  error: Error & { digest?: string };
  reset: () => void;
}) {
  return (
    <div>
      <h2>Something went wrong!</h2>
      <p>{error.message}</p>
      <button onClick={() => reset()}>Try again</button>
    </div>
  );
}

// app/not-found.tsx
export default function NotFound() {
  return (
    <div>
      <h2>404 - Page Not Found</h2>
      <p>The page you are looking for does not exist.</p>
      <a href="/">Go Home</a>
    </div>
  );
}

// Global error (app/global-error.tsx)
"use client";

export default function GlobalError({
  error,
  reset,
}: {
  error: Error;
  reset: () => void;
}) {
  return (
    <html>
      <body>
        <h2>Something went wrong!</h2>
        <button onClick={() => reset()}>Try again</button>
      </body>
    </html>
  );
}
```

---

## API Routes

Server-side API endpoints:

```typescript
// App Router: app/api/users/route.ts
import { NextResponse } from "next/server";

export async function GET() {
  const users = await db.users.findMany();
  return NextResponse.json(users);
}

export async function POST(request: Request) {
  const body = await request.json();
  const user = await db.users.create({ data: body });
  return NextResponse.json(user, { status: 201 });
}

// Dynamic route: app/api/users/[id]/route.ts
export async function GET(
  request: Request,
  { params }: { params: { id: string } }
) {
  const user = await db.users.findUnique({ where: { id: params.id } });
  if (!user) {
    return NextResponse.json({ error: "User not found" }, { status: 404 });
  }
  return NextResponse.json(user);
}

export async function PUT(
  request: Request,
  { params }: { params: { id: string } }
) {
  const body = await request.json();
  const user = await db.users.update({ where: { id: params.id }, data: body });
  return NextResponse.json(user);
}

export async function DELETE(
  request: Request,
  { params }: { params: { id: string } }
) {
  await db.users.delete({ where: { id: params.id } });
  return NextResponse.json(null, { status: 204 });
}
```

---

## Middleware

Intercept requests before they reach routes:

```typescript
// middleware.ts (root level)
import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

export function middleware(request: NextRequest) {
  // Authentication check
  const token = request.cookies.get("token");

  if (!token && request.nextUrl.pathname.startsWith("/dashboard")) {
    return NextResponse.redirect(new URL("/login", request.url));
  }

  // Add custom headers
  const response = NextResponse.next();
  response.headers.set("x-request-id", crypto.randomUUID());

  return response;
}

export const config = {
  matcher: ["/dashboard/:path*", "/api/:path*"],
};
```

---

## ISR, SSR, SSG

### Static Site Generation (SSG)

Pre-render pages at build time:

```typescript
// app/blog/[slug]/page.tsx
export async function generateStaticParams() {
  const posts = await fetch("https://api.example.com/posts").then((res) =>
    res.json()
  );

  return posts.map((post) => ({
    slug: post.slug,
  }));
}

export default async function BlogPost({
  params,
}: {
  params: { slug: string };
}) {
  const post = await fetch(`https://api.example.com/posts/${params.slug}`).then(
    (res) => res.json()
  );

  return <article>{post.content}</article>;
}
```

### Server-Side Rendering (SSR)

Render pages on every request:

```typescript
// app/dashboard/page.tsx (default in App Router)
async function getDashboardData() {
  const res = await fetch("https://api.example.com/dashboard", {
    cache: "no-store", // No caching
  });
  return res.json();
}

export default async function Dashboard() {
  const data = await getDashboardData();
  return <div>{data.content}</div>;
}
```

### Incremental Static Regeneration (ISR)

Revalidate static pages at runtime:

```typescript
// app/products/[id]/page.tsx
async function getProduct(id: string) {
  const res = await fetch(`https://api.example.com/products/${id}`, {
    next: { revalidate: 3600 }, // Revalidate every hour
  });
  return res.json();
}

export default async function Product({
  params,
}: {
  params: { id: string };
}) {
  const product = await getProduct(params.id);
  return <div>{product.name}</div>;
}

// Time-based revalidation
export const revalidate = 60; // Revalidate every 60 seconds

// On-demand revalidation
// app/api/revalidate/route.ts
import { revalidatePath } from "next/cache";
import { NextResponse } from "next/server";

export async function POST(request: Request) {
  const { path } = await request.json();
  revalidatePath(path);
  return NextResponse.json({ revalidated: true });
}
```

---

## Image Optimization

Built-in image component for optimization:

```typescript
import Image from "next/image";

// Basic usage
export default function Hero() {
  return (
    <Image
      src="/hero.jpg"
      alt="Hero image"
      width={1200}
      height={600}
      priority
    />
  );
}

// Remote images
export default function Avatar() {
  return (
    <Image
      src="https://example.com/avatar.jpg"
      alt="User avatar"
      width={100}
      height={100}
      fill
      style={{ objectFit: "cover" }}
    />
  );
}

// Placeholder with blur
export default function BlogImage() {
  return (
    <Image
      src="/blog.jpg"
      alt="Blog image"
      width={800}
      height={400}
      placeholder="blur"
      blurDataURL="data:image/jpeg;base64,..."
    />
  );
}

// Responsive images
export default function Gallery() {
  return (
    <Image
      src="/photo.jpg"
      alt="Photo"
      fill
      sizes="(max-width: 768px) 100vw, 50vw"
    />
  );
}
```

---

## Metadata API

SEO and metadata configuration:

```typescript
// app/page.tsx
import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "My App",
  description: "Built with Next.js",
  openGraph: {
    title: "My App",
    description: "Built with Next.js",
    url: "https://myapp.com",
    siteName: "My App",
    images: [
      {
        url: "https://myapp.com/og.png",
        width: 1200,
        height: 630,
      },
    ],
  },
  twitter: {
    card: "summary_large_image",
    title: "My App",
    description: "Built with Next.js",
    images: ["https://myapp.com/og.png"],
  },
};

// Dynamic metadata
export async function generateMetadata({
  params,
}: {
  params: { id: string };
}): Promise<Metadata> {
  const product = await fetch(
    `https://api.example.com/products/${params.id}`
  ).then((res) => res.json());

  return {
    title: product.name,
    description: product.description,
    openGraph: {
      title: product.name,
      images: [product.image],
    },
  };
}

// Metadata with generateStaticParams
export async function generateStaticParams() {
  const products = await fetchProducts();
  return products.map((product) => ({ id: product.id }));
}
```
