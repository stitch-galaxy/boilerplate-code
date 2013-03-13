//
//  ArtWork.m
//  Cross Stitch Library
//
//  Created by 123 on 18.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "ArtWorkDesign.h"

@implementation ArtWorkDesign

@synthesize Threads = threads;




- (uint32_t) GetWidth
{
    return width;
}

- (uint32_t) GetHeight
{
    return height;
}

- (uint32_t) GetIndexForRow: (uint32_t) row AndColumn: (uint32_t) column
{
    return row * width + column;
}

-(id<IStitch>) GetStitchAtRow: (uint32_t) row AndColumn: (uint32_t) column
{
    return [picture pointerAtIndex:[self GetIndexForRow: row AndColumn:column]];
}


- (void) SetSticth: (id<IStitch>) stitch AtRow: (uint32_t) row AndColumn: (uint32_t) column
{
    [picture replacePointerAtIndex:[self GetIndexForRow: row AndColumn:column] withPointer:(__bridge void *)(stitch)];
}

- (id) initWithWidth: (uint32_t) aWidth AndHeight: (uint32_t) anHeight Threads: (CSGThreadsPalette *) aThreads
{
    if (self = [super init])
    {
        width = aWidth;
        height = anHeight;
        NSPointerFunctionsOptions options = NSPointerFunctionsStrongMemory & NSPointerFunctionsObjectPersonality;
        picture = [[NSPointerArray alloc] initWithOptions: options];
        [picture setCount:aWidth * anHeight];
        threads = aThreads;
    }
    return self;
}

- (void)encodeWithCoder:(NSCoder *)aCoder
{
    //TODO
}

- (id)initWithCoder:(NSCoder *)aDecoder
{
    //TODO
}


@end

@implementation ArtWorkDesign(Serialization)

- (size_t) GetSerializedLength
{
    size_t size =  /*version*/sizeof(uint32_t) +  [threads serializedLength] + /*width and height*/sizeof(uint32_t) * 2;
    for(Stitch* stitch in picture)
    {
        size += [stitch GetSerializedLength];
    }
    return size;
}

- (void) SerializeToBuffer: (void*) buffer
{
    uint32_t *version = (uint32_t *) buffer;
    *version = 1;
    version++;
    
    void *buf = (void *) version;
    size_t length = [threads serializedLength];
    [threads serializeToBuffer:buf];
    buf += length;
    
    uint32_t* wAndH = (uint32_t *) buf;
    *wAndH = width;
    ++wAndH;
    *wAndH = height;
    ++wAndH;
    buf = (void *) wAndH;
    for(Stitch* stitch in picture)
    {
        length = [stitch GetSerializedLength];
        [stitch SerializeToBuffer:buf WithThreadsCollection:threads];
        buf+= length;
    }
}

+ (id) DeserializeFromBuffer: (const void*) buffer
{
    uint32_t *version = (uint32_t *) buffer;
    uint32_t iVersion = *version;
    version++;
    if (iVersion == 1)
    {
        void *buf = (void *) version;
        CSGThreadsPalette *aThreads = [CSGThreadsPalette deserializeFromBuffer:buf];
        buf += [aThreads serializedLength];
        
        uint32_t* wAndH = (uint32_t *) buf;
        uint32_t aWidth = *wAndH;
        ++wAndH;
        uint32_t anHeight = *wAndH;
        ++wAndH;
        buf = (void *) wAndH;
        
        ArtWorkDesign* design = [[ArtWorkDesign alloc] initWithWidth:aWidth AndHeight:anHeight Threads:aThreads];
        
        for(int i = 0; i < aWidth * anHeight; ++i)
        {
            Stitch* stitch = [Stitch DeserializeFromBuffer:buf WithThreadsCollection:aThreads];
            uint32_t row = i / aWidth;
            uint32_t column = i - row * aWidth;
            [design SetSticth: stitch AtRow:row AndColumn:column];
        }
        return design;
    }
    return nil;

}

@end 

