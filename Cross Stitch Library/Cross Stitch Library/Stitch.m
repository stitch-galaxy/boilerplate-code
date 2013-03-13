//
//  Stitch.m
//  Cross Stitch Library
//
//  Created by 123 on 17.02.13.
//  Copyright (c) 2013 Tarasov Evgeny. All rights reserved.
//

#import "Stitch.h"

@implementation Stitch

@synthesize Done = done;

@synthesize ThreadMaterial = threadMaterial;
@synthesize ThreadStitchType = threadStitchType;


- (id) initWithThreadMaterial: (CSGThread*) aThreadMaterial ThreadStitchType: (int8_t) aThreadStitchType
{
    if (self = [super init])
    {
        self.ThreadMaterial = aThreadMaterial;
        self.ThreadStitchType = aThreadStitchType;
    }
    return self;
}


- (BOOL) isEqual: (id) object
{
    if (object == self)
    {
        return YES;
    }
    if (!object || ![object isKindOfClass: [Stitch class]])
    {
        return NO;
    }
    return [self isEqualToStitch: object];
}

- (BOOL) isEqualToStitch: (Stitch*) aStitch
{
    if (self == aStitch)
    {
        return YES;
    }
    if (![threadMaterial isEqual: aStitch.ThreadMaterial])
    {
        return NO;
    }
    if (threadStitchType != aStitch.ThreadStitchType)
    {
        return NO;
    }
    if (![threadMaterial isEqual: aStitch.ThreadMaterial])
    {
        return NO;
    }
    return YES;
}

- (NSUInteger) hash
{
    NSUInteger hash = 17;
    hash = 31 * hash + threadMaterial.hash;
    hash = 31 * hash + threadStitchType;
    return hash;
}

@end


@implementation Stitch (Serialization)

- (size_t) GetSerializedLength
{
    size_t size = sizeof(int8_t) + sizeof(int8_t) + sizeof(uint32_t) + sizeof(uint32_t) + sizeof(BOOL);
    return size;
}

- (void) SerializeToBuffer: (void*) buffer WithThreadsCollection: (CSGThreadsPalette*) threads
{
    uint8_t *buf = (uint8_t*) buffer;
    *buf = threadStitchType;
    
    ++buf;
    uint32_t *iBuf = (uint32_t *) buf;
    uint32_t threadMaterialsIndex = [threads threadIndex: self.ThreadMaterial];
    *iBuf = threadMaterialsIndex;
}

+ (id) DeserializeFromBuffer: (void*) buffer WithThreadsCollection: (CSGThreadsPalette*) threads
{
    uint8_t *buf = (uint8_t*) buffer;
    int8_t aThreadStitchType = *buf;
    
    ++buf;
    int8_t aBeadStitchType = *buf;
    
    ++buf;
    uint32_t *iBuf = (uint32_t *) buf;
    uint32_t threadMaterialsIndex = *iBuf;
    CSGThread* aThreadMaterial = [threads threadByIndex:threadMaterialsIndex];
    
    ++iBuf;    uint32_t beadMaterialsIndex = *iBuf;

    
}

@end