import React from 'react'
import { twMerge } from 'tailwind-merge'

export interface CardProps extends React.HTMLAttributes<HTMLDivElement> {
  children: React.ReactNode
  onClick?: () => void
  elevated?: boolean
  padding?: 'xs' | 'sm' | 'md' | 'lg' | 'xl'
}

const paddingMap: Record<string, string> = {
  xs: 'p-2',
  sm: 'p-3',
  md: 'p-4',
  lg: 'p-5',
  xl: 'p-6',
}

const CardRoot: React.FC<CardProps> = ({
  children,
  className = '',
  onClick,
  elevated = false,
  padding,
  ...props
}) => {
  const shadowClass = elevated ? 'shadow-lg' : 'shadow'
  const clickableStyles = onClick ? 'cursor-pointer hover:opacity-80 transition-opacity' : ''
  const paddingClass = padding ? paddingMap[padding] : ''

  return (
    <div
      onClick={onClick}
      className={twMerge(
        'bg-white rounded-lg border border-slate-200',
        shadowClass,
        paddingClass,
        clickableStyles,
        className,
      )}
      {...props}
    >
      {children}
    </div>
  )
}

interface CardHeaderProps extends React.HTMLAttributes<HTMLDivElement> {
  children: React.ReactNode
}

const CardHeader: React.FC<CardHeaderProps> = ({ children, className = '', ...props }) => {
  return (
    <div className={twMerge('px-6 py-4 border-b border-slate-200', className)} {...props}>
      {children}
    </div>
  )
}

interface CardBodyProps extends React.HTMLAttributes<HTMLDivElement> {
  children: React.ReactNode
}

const CardBody: React.FC<CardBodyProps> = ({ children, className = '', ...props }) => {
  return (
    <div className={twMerge('px-6 py-4', className)} {...props}>
      {children}
    </div>
  )
}

interface CardFooterProps extends React.HTMLAttributes<HTMLDivElement> {
  children: React.ReactNode
}

const CardFooter: React.FC<CardFooterProps> = ({ children, className = '', ...props }) => {
  return (
    <div className={twMerge('px-6 py-4 border-t border-slate-200', className)} {...props}>
      {children}
    </div>
  )
}

interface CardTitleProps extends React.HTMLAttributes<HTMLHeadingElement> {
  children: string
}

const CardTitle: React.FC<CardTitleProps> = ({ children, className = '', ...props }) => {
  return (
    <h3 className={twMerge('text-[26px] font-semibold text-slate-900', className)} {...props}>
      {children}
    </h3>
  )
}

interface CardTextProps extends React.HTMLAttributes<HTMLParagraphElement> {
  children: string
}

const CardText: React.FC<CardTextProps> = ({ children, className = '', ...props }) => {
  return (
    <p className={twMerge('text-slate-700', className)} {...props}>
      {children}
    </p>
  )
}

export const Card = Object.assign(CardRoot, {
  Header: CardHeader,
  Body: CardBody,
  Footer: CardFooter,
  Title: CardTitle,
  Text: CardText,
})
